# 웹 애플리케이션 개요

## 1. 🚀 프로젝트 이름

- Ezen Book Store (<http://ezbook.store>)

## 2. 📖 프로젝트 개요

- **목적**: 기업 요구사항에 맞춘 온라인 서점 시스템 개발 및 포트폴리오 활용
- **주요 목표**:
  - 참여기업 요구사항 지시에 맞춘 각 시스템별 기능 구현
  - 유저 / 관리자 SIDE별 기능
  - 회원가입, 로그인, 내정보수정 기능 - (Google, Naver, Kakao 등 Synchro 로그인)
  - 장바구니, 구매기록 기능
  - 배송비 시스템 - (주문/교환/반품) - 무료 / 조건부 무료
  - 결제 및 조회 기능 - (PG사 연동)
  - 적립금 시스템
  - 상품등록 및 관리 기능
  - 게시판 등록 및 관리 기능 - (상품 리뷰, 공지사항 등)

## 3. 🔧 기술 스택

### 개발 도구

![VS Code](https://img.shields.io/badge/IDE-VS%20Code-blue?logo=visualstudiocode&logoColor=white) ![IntelliJ](https://img.shields.io/badge/IDE-IntelliJ%20IDEA-orange?logo=intellijidea&logoColor=white)

### 프론트엔드

![HTML](https://img.shields.io/badge/HTML-E34F26?logo=html5&logoColor=white) ![CSS](https://img.shields.io/badge/CSS-1572B6?logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black) ![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?logo=bootstrap&logoColor=white)

### 백엔드

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-6DB33F?logo=hibernate&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black)

### 데이터베이스

![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white) ![AWS RDS](https://img.shields.io/badge/AWS%20RDS-232F3E?logo=amazonaws&logoColor=white)

### 협업 도구

![GitHub](https://img.shields.io/badge/GitHub-181717?logo=github&logoColor=white)

### 기타 도구

![Jenkins](https://img.shields.io/badge/Jenkins-D24939?logo=jenkins&logoColor=white) ![Git](https://img.shields.io/badge/Git-F05032?logo=git&logoColor=white) ![AWS](https://img.shields.io/badge/AWS-232F3E?logo=amazonaws&logoColor=white)

## 4. 👥 참여자 및 역할

- **김태현**:
  - 페이지
- **채윤성**:
  - 페이지
- **이정인**:
  - 페이지
- **정재환**:
  - 페이지
- **황예주**:
  - 페이지

## 5. 📜 주요 기능 설명

### 사용자 페이지

- **회원가입 및 로그인**
  - 일반 회원가입 및 간편 로그인 지원
- **내 정보 수정**
- **장바구니**
- **구매 기록 조회**
- **적립금 시스템**
  - 상품 구매 및 이벤트 참여 시 적립금 지급
  - 일정 적립금 이상 시 구매에 사용 가능

### 관리자 페이지

- **상품 등록 및 관리**
- **게시판 등록 및 관리**
- **배송비 시스템**
  - 일정 가격 이상 주문 시 배송비 감면
- **결제 및 조회**
  - 결제 대행 API(KG 이니시스 등) 활용

### 추가 기능

- **검색 기능** :
  - 통합 검색, 제목/저자 검색 등
- **상품 설명** :
  - 상세한 상품 옵션 및 설명 제공
- **공지사항 및 이벤트 페이지** :
- **고객센터** :
  - Q&A, 문의하기
- **아이디/비밀번호 찾기** :
- **화면 네비게이션** :
  - 상단 이동 버튼

## 6. 아키텍처 다이어그램

- **아키텍처 다이어그램 (개발자 다이어그램)**: ?

![아키텍처 다이어그램](./read.me.image/00architecturediagram.png)

- **아키텍처 다이어그램 (흐름도 - 유저)**: ?

![아키텍처 다이어그램](./read.me.image/01architecturediagram.png)

- **아키텍처 다이어그램 (흐름도 - 관리자)**: ?

![아키텍처 다이어그램](./read.me.image/02architecturediagram.png)

## 7. 화면 예시

- **메인 페이지**: 사용자가 처음 접속하면 출력되는 페이지

  ![메인 페이지](./read.me.image/00main.PNG)

- **상품 메인 페이지**: 카테고리별 또는 검색을 통해 상품을 볼 수 있는 상품 메인 페이지

  ![상품 메인 페이지](./read.me.image/01bookproduct.PNG)

- **상품 디테일 페이지**: 상품 클릭시 상품의 상세정보를 볼 수 있는 상품 디테일 페이지

  ![상품 디테일 페이지](./read.me.image/01bookdetail.png)

- **회원가입 페이지**: 회원가입을 할 수 있는 페이지

  ![회원가입 페이지](./read.me.image/02signup.png)

- **로그인 페이지**: 회원가입 후 로그인 또는 간편 소셜로그인을 할 수 있는 페이지

  ![로그인 페이지](./read.me.image/03login.PNG)

- **마이페이지 - 나의 정보**: 가입한 정보를 확인하고 수정할 수 있는 페이지

  ![마이페이지- 나의정보](./read.me.image/04mypage_profile.png)

- **장바구니 페이지**: 담은 상품을 확인하고 구매할 수 있는 페이지

  ![장바구니 페이지](./read.me.image/05Cart.png)

- **주문 확인 모달**: 주문하기 전 정보를 확인하고 구매할 수 있는 모달창

  ![주문 확인 모달](./read.me.image/05order.PNG)

- **PG연동 모달**: 결제시 PG사 연동하여 결제할 수 있는 모달창

  ![페이지](./read.me.image/05PG_Pay.PNG)

- **마이페이지 - 주문내역**: 주문한 정보를 확인할 수 있는 페이지

  ![마이페이지 - 주문내역](./read.me.image/06mypage_orderlist.PNG)

- **주문내역 상세 모달**: 주문내역을 상세하게 확인할 수 있는 페이지

  ![주문내역 상세 모달](./read.me.image/07orderlist_check.PNG)

- **관리자 페이지 - 대시보드**: 관리자페이지에 처음 접속하면 한 눈에 확인하고 관리할 수 있는 페이지

  ![관리자 페이지 - 대시보드](./read.me.image/8Admin_Dashboard.png)

- **관리자 페이지 - 상품 관리 페이지**: 상품을 등록하고 조회, 관리할 수 있는 페이지

  ![관리자 페이지 - 상품 관리 페이지](./read.me.image/9Admin_Product.png)

- **관리자 페이지 - 이벤트 관리 페이지**: 이벤트를 등록하거나 관리할 수 있는 페이지

  ![관리자 페이지 - 이벤트 관리 페이지](./read.me.image/10Admin_Event.png)

- **최근 본 도서 확인**: 이벤트를 추가할 수 있는 모달창

  ![최근 본 도서 확인](./read.me.image/10Event_add.PNG)

- **페이지 네비게이션 버튼**: 홈으로 돌아가기 / 상단으로 이동 / 관리자모드 / 최근 본 상품목록 버튼

  ![페이지 네비게이션 버튼](./read.me.image/Detail_Buttun.PNG)

## 8. 📅 참고 사이트

- [교보 문고](https://www.kyobobook.co.kr/) : UI 참고
- [예스24](https://www.yes24.com/main/default.aspx) : 참고
- [바로보네](https://www.barovone.com/kr/index/index.lime) : 참고
- [반디앤루디스](https://www.bandinlunis.com/front/main.do) : 참고
- [알라딘](https://www.aladin.co.kr/home/welcome.aspx) : 참고

---

## 부록

- **참조 문서**:
  - 협업 문서 관리 : [GoogleSheets](https://docs.google.com/spreadsheets/d/1ABl90LxOWC4B3PIknlzmOfYaD1EMgwfHcxZY2cWX2GE/edit?gid=244139402#gid=244139402)
- **연락처**:
  - 김태현 : [Email](mailto:qsdcv301@naver.com), [GitHub](https://github.com/qsdcv301)
  - 채윤성 : [Email](mailto:gksmsk5094@gmail.com), [GitHub](https://github.com/ChaiTope)
  - 이정인 : [Email](mailto:dlwjddls888@gmail.com), [GitHub](https://github.com/GreatOvOb)
  - 정재환 : [Email](mailto:jjjhhh2569@gmail.com), [GitHub](https://github.com/JaeHwan2569)
  - 황예주 : [Email](mailto:jooland05@gmail.com), [GitHub](https://github.com/HwangYeJoo)