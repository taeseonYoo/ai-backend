# AI - BackEnd 연동

AI API를 연동하는 간단한 대화형 서비스입니다.
사용자의 질문을 저장하고, AI 응답을 제공합니다.

## 기술 스택
- Kotlin + Spring Boot 3.2
- Spring Data JPA
- PostgreSQL 15.8
- Spring Security
- OpenAI API (gpt-3.5 / gpt-4)
- WebClient
- Spring Retry
- Gradle

## 구현 기능
✅ 질문 등록 (POST "api/chats")
- 유저의 입력을 기반으로 질문 생성
- 최근 30분 이내 생성된 스레드가 없을 경우 새로운 Thread 자동 생성
- 스트리밍 응답 처리 방식 선택
- AI 모델 선택

✅ AI 응답 처리
- 질문 생성 시 ChatRequestedEvent 발행 → 비동기 리스너에서 AI 호출
- @Retryable 사용하여 실패 시 자동 재시도
- 응답 저장 시 상태를 COMPLETED, 실패 시 FAILED 구분

✅ 대화 히스토리 조회 (GET "api/chats/history")
- 로그인된 유저의 모든 스레드를 가져온다.
- 정렬 및 페이징 지원
- ROLE_ADMIN일 경우 조회 가능

✅ 스레드 삭제 (DELETE "api/chats/{id}/delete")
- 선택한 스레드를 삭제한다.


## 회고
요구사항과 전체적인 시스템의 흐름을 파악하기 위하여 공책을 활용해서 개발했습니다.
주 사용 언어는 Java지만 과제에서 코틀린을 사용해야 했기에
java 기반으로 로직을 생각하고, 코드를 코틀린으로 변환하기 위해서 chatGPT를 사용했습니다.

AI 요청 처리 방식 및 설계에 집중 했던 것 같습니다.
외부 API 요청 시, 응답 지연 및 장애가 발생하면 전체 트랜잭션에 영향을 줄 수 있는 문제를 인지하고 있었습니다.

사용자가 질문을 입력하면, 우선 질문을 Chat 엔티티로 저장하고, 트랜잭션이 성공적으로 커밋된 이후(AFTER_COMMIT)에 AI 요청 이벤트가 발행되도록 구현하였습니다.
실패할 경우를 대비해 Spring Retry를 적용하여 자동으로 재시도 되도록 처리했습니다.
