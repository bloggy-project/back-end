# summer
simple blog project


### JPQL로 작성된 쿼리를 queryDSL로 교체(2023.04.22)

#### JPQL의 문제점
- JPQL은 문자열(=String) 형태이기 때문에 개발자 의존적 형태
- Compile 단계에서 Type-Check가 불가능
- RunTime 단계에서 오류 발견 가능 (장애 risk 상승)

다음과 같은 JPQL의 문제를 해결하기 위해 queryDSL로 교체했습니다.

#### queryDSL 특징
- 문자가 아닌 코드로 작성
- Compile 단계에서 문법 오류를 확인 가능
- 코드 자동 완성 기능 활용 가능
- 동적 쿼리 구현 가능


### 블로그 태그 기능 추가 후 다대다 연관관계를 Status로 관리 (2023.04.30 )


1. 벨로그는 게시물의 태그가 새로 만들어져도 기존에 존재하지 않던 태그는
바로 생성안됨.

2. 벨로그는 게시물을 만든 뒤, 기존 태그를 삭제하여 포스트를 수정하여도 바로 반영안됨.
게시물에는 태그가 보이지 않지만 바로 삭제되지 않고 목록에 남아있는 경우가 있다.

다음의 2가지 조건을 참조하여 구현해 보았다.

	
### Post 생성 시 
	
태그 존재하면 REGISTERED 하고 연관관계 설정하고 생성.
태그 존재하지 않으면 연관관계 설정하고 UPDATED 그리고 TAG는 생성 아직 하지않음.

```java
for (String tagName : tagNames) {
            tagRepository.findByName(tagName).ifPresentOrElse(
                    (tag)->{
                        postTags.add(createPostTag(post, tag, tagName, PostTagStatus.REGISTERED));
                    },
                    ()->{
                        postTags.add(updatePostTag(post, tagName, PostTagStatus.UPDATED));
                    }
            );
        }
```

### Post 수정 시
	
#### 기존에 존재하던 태그를 삭제하고, 새로운 태그를 추가하는 경우
1.옛날 태그 리스트에서 기존에 존재하던 태그가 없어지는 것을 새로 들어온 태그들과 비교하는 로직은 
시간복잡도: N*M
	
2.Old 태그 리스트들을 삭제하고 새로 태그 생성은 시간복잡도: N+M 

단순하게 봤을 땐 2번 방식이 좋아보인다.

#### DB 쿼리는 어떻게 되는지?

#### 1번 방식
PostTag N개중 NewTag M개와 비교하여 겹치지 않는 태그
연관관계 지우고
Deleted로 Status 변경 (나중에 쿼리)
기존 태그중에 UPDATED와 REGISTERED의 태그들 경우 살아남음.

새로 생성된 태그네임들 PostTag 생성하고 
Status는 이미 존재하는 Tag면 Post Tag둘다 연관관계 설정후 Status REGISTERED.
존재하지 않는 Tag면 Post만 연관관계 설정후 Status UPDATED.
PostTag 개수 만큼 매번 save? -> saveAll() 으로 한번에 처리가능.
	
- 장점: List의 데이터 삭제 삽입이 2번 방식에 비해 적음. 
		연관관계 설정 비용이 적게듬.(최악의 경우 OldTag개수 만큼 삭제 삽입 이지만 기존 태그를 다 지우고 새로 수정하는 케이스가 과연그렇게 많을지..?)

- 단점: N*M의 연산
#### 2번 방식

Post의 PostTag N개의 연관관계 지우고
Deleted로 Status 변경 (나중에 쿼리)
새로 생성된 태그네임들 PostTag 생성하고
Status는 이미 존재하는 Tag면 Post Tag둘다 연관관계 설정후 Status REGISTERED.
존재하지 않는 Tag면 Post만 연관관계 설정후 Status UPDATED.
PostTag 개수 만큼 매번 save? -> saveAll() 으로 한번에 처리가능.

- 장점: N+M 연산. 

- 단점: List의 데이터 삭제 삽입이 OldTag(N)개수만큼 존재. 연관관계 설정 비용이 많이 듬.
	
Status가 UPDATED인 PostTag들은 주기적으로 Tag들을 saveAll() 연산으로 처리한 뒤 REGISTERED로 변경.
Status가 DELETED인 PostTag들은 주기적으로 Delete 벌크 연산(연관관계를 미리 정리해야 함)
	
### 궁금증 연관관계를 포기하고 List 연산 비용을 줄인다면?
	
Tag별 Post들을 fetch join으로 가져오지 않는 경우. -> 쿼리 +1
Post의 Tag들을 fetch join으로 가져오지 않는 경우. -> 쿼리 +1
	
처음엔 연관관계 연산 비용을 줄이기 위해 List를 삭제했었는데, 장기적으로 봤을때 
쿼리 연산 갯수가 많이 늘어날 것으로 보인다. 
	
### 결론
Logic이 복잡해지더라도 연관관계를 설정하고, 
1번방식의 N*M의 시간복잡도를 손해보는 것 같지만, List 삭제 삽입 연산 비용을 감안했을 때, 
	
1번방식 + 연관관계 설정의 조합으로 코드를 작성하는 것이 장기적으로 비용이 제일 적게 들 것이라고 판단했다.
	
    
    
### 스케쥴러를 이용한 Status 정리


```java
@Scheduled(cron ="0 0/10 * * * ?")
    public void registerUpdateStatusPost(){
        //이 부분 나중에 쿼리 1개로 최적화 여지 있을듯
        List<String> tagNames = postTagRepository.findTagNamesByStatusUpdate();
        List<PostTag> postTags = postTagRepository.findAllStatusUpdate();
        for (String tagName : tagNames) {
            Tag tag = Tag.builder()
                    .name(tagName)
                    .build();
            tagRepository.save(tag);
            postTags.forEach((pt)->{
                if (pt.getTagName().equals(tag.getName())) {
                    pt.setTag(tag);
                    pt.setStatus(PostTagStatus.REGISTERED);
                }
            });
        }
        postTagRepository.saveAll(postTags);
    }
    @Scheduled(cron ="0 0/10 * * * ?")
    public void deleteDeleteStatusPost(){
        postTagRepository.deleteAllStatusDelete();
    }
 ```
 
다음과 같이 UPDATE가 필요한 태그의 이름들은, 중복되지 않게 가져와서 생성을 하고,
생성된 태그와 이름이 동일한 UPDATED STATUS의 PostTag에 saveAll을 사용하여, 업데이트를 해준다.

saveAll의 쿼리 갯수가 건당 1개씩 발생하는지 추후에 체크해봐야함.



### AccessToken방식을 RefreshToken방식으로 교체

- Redis, ControllerAdvice, Interceptor를 활용

### 문제점
- 기존에 적용된 jwt Token은 access token 1개만 사용하며, 만료기간이 하루이므로 탈취와 같은 보안문제에 취약
### 목표
- refresh token을 적용하여 보안문제를 개선.

### 고려요소

#### RefreshToken의 SignKey와 AccessToken의 SignKey는 다르게 관리

- Access Token과 RefreshToken의 SignKey가 같을 경우, RefreshToken을 탈취하여, Access Token처럼 활용가능할 수 있음.

#### RefreshToken은 User당 1개만 할당

Key를 UserId가 아닌 RefreshToken의 Value로 할당하고, Value를 UserId로 할당하는 경우

##### 장점
- UserId의 활용이 편리함.

##### 단점
- User마다 RefreshToken을 여러 개 지닐 수 있음. 즉, Refresh Token 탈취범도 문제없이 Refresh Token을 사용할 수 있음.

Key를 UserId, Value를 RefreshToken Value로 할당하는 경우

##### 장점
- User마다 RefreshToken을 1개만 지닐 수 있음. 만약 탈취범이 RefreshToken을 탈취해서 사용가능 한 경우, 진짜 User는 Login을 해야 서비스를 이용할 수 있음. User가 Login을 하면 탈취범은 탈취한 RefreshToken을 더이상 이용 불가능.

##### 단점
- Redis방식은 Value만으로 Key값,즉 UserId를 알 수 없으므로, RefreshToken의 Value에 UserId 정보를 넣거나, Redis의 DB 보조 인덱스 전략이 필요함.
- 필자는 RefreshToken의 Value에 Jwt(String)값을 넣어 UserId를 Value 자체적으로 알 수 있게 구현함.

#### AccessToken을 갱신할 때, RefreshToken도 새로 발급

- Refresh Token Rotation, 이렇게 구성하면 Refresh Token은 1회용이 되고, DB에 이미 사용된 Refresh Token정보를 추가적으로 관리한다면, 재사용된 Token을 추적하고 이 Refresh Token을 발급 받았던 User에게 알려줄 수 있음.
	
### 자세한 설명 블로그 링크 첨부
- https://velog.io/@gon109/Refresh-Token-%EC%A0%81%EC%9A%A9%EA%B3%BC%EC%A0%95-%EC%9E%91%EC%84%B1%EC%A4%91