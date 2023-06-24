# back-end
백엔드 개발 담당 리포지토리

### 잔디 심기 전략 예시
#### hg repo : main -> dev -> feature/token
- git checkout feature/token
- git rebase dev
- git checkout dev
- git merge feature/token
- 	
$ git checkout master
$ git merge --squash my-branch
$ git commit -m "your-commit-message"

### 하위 branch 전략
- 새로운 기능.
- feature: 기능개발
- feature/{구현기능명}


### Commit 전략 
- Feat: 기능이 추가됐을 때
- Fix: 기능에 발생한 오류를 수정한 경우
- Style: 코드 스타일 수정, 코드에 별다른 수정이 없는 경우
- Design: UI의 디자인을 수정한 경우, CSS를 수정한 경우
- Docs: README 등 문서를 추가 및 수정, 삭제
- Refactor: 코드를 리팩토링한 경우
- Chore: 설정을 변경한 경우(코드의 변경 없이)
