# 뉴스피드 프로젝트 (16조)

## 프로젝트 정보
| 구분     | 내용                                                   |
|--------|------------------------------------------------------|
| 기간     | 2025.02.14 ~ 2025.02.20 (6일)                                |
| 팀원     | 최다원(팀장), 김정호, 김종연, 홍수경                                |
| 설명     | User, Schedule, Comment로 구성된 일정 관리 앱의 백엔드 구성.        |
| 사용 기술  | Java, Spring MVC, JPA, Spring Data JPA, MySQL                 |
| JDK    | Amazon Corretto 17.0.14                    |
| Spring | Boot 3.4.2, Core 6.2.2                               |
  
<br/><br/>

## 역할 분담

| 팀원     | 내용                                                   |
|--------|------------------------------------------------------|
| 최다원   | 로그인, 회원가입, 회원 조회, 회원 삭제                             |
| 김정호   | 게시글 CRUD, 조회수 추적                             |
| 김종연   | 사용자 프로필 CRUD, 친구 기능, 기타 코드 전반                             |
| 홍수경   | 댓글 CRUD, 댓글 수                             |

<br/><br/>

## 와이어 프레임
- <b>로그인 시퀀스</b>
<img width="80%" alt="로그인 시퀀스" src="https://github.com/user-attachments/assets/7e27e066-c988-4e48-aea3-4b87cb5be10f" />

- <b>메인 시퀀스</b>
<img width="80%" alt="메인 시퀀스" src="https://github.com/user-attachments/assets/987db3be-0dc4-4ed2-b10f-262951824b9b" />

<br/><br/>

## ERD
<image src="https://github.com/user-attachments/assets/491dc48a-4427-4b05-98c1-7483276d023e" width="80%"></image>

<br/><br/>

## API 명세서
<br/>

<details>
  <summary><b>유저</b></summary>
  <br/>
  <div>
    <image src="https://github.com/user-attachments/assets/f7f3576e-29c0-4619-bf19-29c650626205" width="80%"></image>
  </div>
</details>

<details>
  <summary><b>프로필</b></summary>
  <br/>
  <div>
    <image src="https://github.com/user-attachments/assets/01e060bd-c048-4b8a-9dd5-bb4e23c92f46" width="80%"></image>
    <image src="https://github.com/user-attachments/assets/9024f15e-9142-4adc-a216-a28d53e98c03" width="80%"></image>
  </div>
</details>

<details>
  <summary><b>친구</b></summary>
  <br/>
  <div>
    <image src="https://github.com/user-attachments/assets/b3c2d168-5048-40d0-9d4f-1e34eae83b83" width="80%"></image>
    <image src="https://github.com/user-attachments/assets/ad0d85eb-a96b-4871-a064-b91e094b9a1d" width="80%"></image>
  </div>
</details>

<details>
  <summary><b>게시글</b></summary>
  <br/>
  <div>
    <image src="https://github.com/user-attachments/assets/e9eea4c5-ec36-4cf0-8717-a6548a84fd08" width="80%"></image>
    <image src="https://github.com/user-attachments/assets/c98d24e0-b232-47c3-ae26-a34bdd589435" width="80%"></image>
    <image src="https://github.com/user-attachments/assets/53329fee-57f4-4b91-b055-897f80d533c2" width="80%"></image>
    <image src="https://github.com/user-attachments/assets/e9fefa3b-788d-4539-9f58-c6e3a59ae877" width="80%"></image>
  </div>
</details>

<details>
  <summary><b>댓글</b></summary>
  <br/>
  <div>
    <image src="https://github.com/user-attachments/assets/0087623e-3770-47f5-878c-06c9b9d4827d" width="80%"></image>
    <image src="https://github.com/user-attachments/assets/dab37dcc-6661-4cb1-87d5-e20096843723" width="80%"></image>
  </div>
</details>

