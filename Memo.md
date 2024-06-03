- ~~Book entity에 category를 포함, 더 추가해야할 것 있는지 파악해서 추가~~
- ~~BookResponseDto를 BookSearchResponseDto와 BookSpecificResponseDto로 나누어 필요한 정보만 요청하게(특히 상세정보)~~
- ~~Product를 부모, Book과 Ticket을 자식으로 상속화~~
- 엔티티 수정사항 ERD 적용해서 재배포
- Custom 예외처리 기준 정하기
- 예외처리는 어디서 할건지 기준 정하기
- ~~(5/31) 메모 시작일과 유효기간 정해서 작성하기~~


- (6/3) 설계를 제대로 안하니 코드가 산으로가고, TDD가 뭔지도 모르면서 테스트 코드 짜다가 성공이 안되어 현타가 온다. 일단 미룬 메모부터 처리하고 해결방법을 찾아보자.
- (6/3) 도서 엔티티 수정에 따른 서비스, 테스트 수정필요