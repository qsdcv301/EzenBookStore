$(document).ready(function () {
    $('.loginCheck').click(function (e) {
        e.preventDefault();
        if (confirm("로그인이 필요합니다. 로그인 페이지로 이동할까요?")) {
            location.replace("/login");
        }
    });

    $('.price').each(function () {
        // 현재 요소의 텍스트 값을 숫자로 변환
        let price = parseInt($(this).text().replace(/[^0-9]/g, ''), 10);

        // 숫자를 천 단위로 구분하고 "원"을 붙임
        let formattedPrice = price.toLocaleString("ko-KR") + "원";

        // 요소의 텍스트를 포맷된 값으로 업데이트
        $(this).text(formattedPrice);
    });

    //    customerService
    $('#userQnASelect').change(function () {
        const sort = $('#userQnASelect option:selected').val();

        // 현재 URL에서 qnaPage 값을 추출
        const urlParams = new URLSearchParams(window.location.search);
        const qnaPage = urlParams.has('qnaPage') ? urlParams.get('qnaPage') : 0;

        // 새로운 URL로 리디렉션
        window.location.href = `/customerService?qnaPage=${qnaPage}&sort=${sort}`;
    });

    //    questionModal
    $(".questionDetailBtn").click(function () {
        const questionId = $(this).data("id");

        // AJAX 요청을 통해 상세 데이터 가져오기
        $.ajax({
            url: `/qna/${questionId}`,
            type: 'POST',
            success: function (response) {
                if (response.success === "true") {
                    // 모달에 데이터 채우기
                    $("#inquiryTypeDetail").val(response.category);
                    $("#memberNameDetail").val(response.name);
                    $("#memberEmailDetail").val(response.email);
                    $("#phoneNumberDetail").val(response.tel);
                    $("#inquiryTitleDetail").val(response.title);
                    $("#inquiryContentDetail").text(response.question);

                    // 답변 여부에 따라 표시
                    if (response.answer && response.answer !== "") {
                        $("#adminReplyArea").show();
                        $("#adminReplyContent").text(response.answer);
                        $("#adminReplyAreaWaiting").hide();
                    } else {
                        $("#adminReplyArea").hide();
                        $("#adminReplyAreaWaiting").show();
                    }
                } else {
                    alert("문의 작성에 실패했습니다.");
                }
            },
            error: function (xhr, status, error) {
                console.error("서버 오류가 발생했습니다.");
            }
        });
    });

    $('.questionModalBtn').click(function (e) {
        e.preventDefault();
        const email = $(this).closest('.questionModal').find('#memberEmail').val();
        const category = $(this).closest('.questionModal').find('#inquiryType').val();
        const title = $(this).closest('.questionModal').find('#inquiryTitle').val();
        const question = $(this).closest('.questionModal').find('#inquiryContent').val();
        if (category === "0") {
            alert("문의 유형을 선택해주세요.");
            return;
        }
        if (title.trim() === "") {
            alert("제목을 작성 해주세요.");
            return;
        }
        if (question.trim() === "") {
            alert("문의 내용을 작성 해주세요.");
            return;
        }
        $.ajax({
            url: '/qna/add',
            type: 'POST',
            data: {
                email: email,
                category: category,
                title: title,
                question: question
            },
            success: function (response) {
                if (response.success) {
                    alert("문의를 작성 했습니다.");
                    location.reload();
                } else {
                    alert("문의 작성에 실패했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    })

    //     cart
    const shippingFeeThreshold = 15000; // 배송비가 무료가 되는 기준 금액
    const baseShippingFee = 3000; // 기본 배송비

    $('.edit-delete-buttons .cart-edit').on('click', function () {
        const $cartItem = $(this).closest('.cart-item');
        $cartItem.find('.quantity').prop("readonly", false); // readonly를 해제
        $(this).closest('.cart-item').find('.edit-delete-buttons').hide();
        $(this).closest('.cart-item').find('.save-cancel-buttons').show();
    });

    // 취소 버튼 클릭 시
    $('.save-cancel-buttons .cart-cancel').on('click', function () {
        const $cartItem = $(this).closest('.cart-item');
        $cartItem.find('.quantity').prop("readonly", true); // readonly를 설정
        $(this).closest('.cart-item').find('.save-cancel-buttons').hide();
        $(this).closest('.cart-item').find('.edit-delete-buttons').show();
    });

    // 전체 선택/해제
    $("#selectAll").click(function () {
        const isChecked = $(this).is(":checked");
        $(".cart-checkbox").prop("checked", isChecked);
        updateSummary(); // 주문 요약 업데이트
    });

    // 개별 체크박스 클릭 시 전체 선택 체크박스 상태 동기화
    $(".cart-checkbox").click(function () {
        const allChecked = $(".cart-checkbox").length === $(".cart-checkbox:checked").length;
        $("#selectAll").prop("checked", allChecked);
        updateSummary(); // 주문 요약 업데이트
    });

    // 수량 변경 시 주문 요약 업데이트
    $(".quantity").on("input", function () {
        updateSummary();
    });

    // 선택 삭제 버튼 클릭 이벤트
    $("#deleteSelected").click(function () {
        const selectedIds = $(".cart-checkbox:checked").map(function () {
            return $(this).closest(".cart-item").find(".cart-delete").attr("data-cart-id");
        }).get();

        if (selectedIds.length === 0) {
            alert("삭제할 상품을 선택해주세요.");
            return;
        }

        deleteCartItems(selectedIds);
    });

    // 카트 업데이트 버튼 클릭 이벤트
    $(".cart-update").click(function () {
        const cartId = $(this).attr("data-cart-id");
        const quantity = $(this).closest(".paymentModal-Data").find(".quantity").val();
        $.ajax({
            url: '/cart/update',
            type: 'POST',
            traditional: true, // 배열을 쿼리 문자열로 전송하기 위해 추가
            data: {
                cartId: [cartId],
                quantity: [quantity]
            },
            success: function (response) {
                if (response.success) {
                    alert("해당항목을 수정 되었습니다.");
                    location.reload();
                } else {
                    alert("해당항목을 수정 실패했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    // 개별 삭제 버튼 클릭 이벤트
    $(".cart-delete").click(function () {
        const cartId = $(this).attr("data-cart-id");
        deleteCartItems([cartId]);
    });

    // AJAX를 통한 선택된 항목 삭제 함수
    function deleteCartItems(cartIds) {
        $.ajax({
            url: '/cart/delete',
            type: 'POST',
            traditional: true,
            data: {cartId: cartIds},
            success: function (response) {
                if (response.success) {
                    alert("선택된 상품이 삭제되었습니다.");
                    location.reload();
                } else {
                    alert("상품 삭제에 실패했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    }

    // 주문 요약 업데이트 함수
    function updateSummary() {
        let totalItems = 0;
        let totalPrice = 0;
        let totalDiscount = 0;

        $(".cart-checkbox:checked").each(function () {
            const cardBody = $(this).closest(".card-body");

            // 가격과 할인율, 수량을 추출하고 숫자로 변환 (기본값을 0으로 설정)
            const price = parseInt(cardBody.find(".price").text().replace(/[^0-9]/g, "")) || 0;
            const discountPercent = parseFloat(cardBody.find(".discount").text().replace(/[^0-9.]/g, "")) || 0;
            const quantity = parseInt(cardBody.find(".quantity").val()) || 0;

            // 개별 항목 계산
            const itemTotal = price * quantity;
            const itemDiscount = itemTotal * (discountPercent / 100);

            totalItems += quantity;
            totalPrice += itemTotal;
            totalDiscount += Math.floor(itemDiscount);
        });

        // 배송비 계산
        let shippingFee = (totalPrice - totalDiscount >= shippingFeeThreshold) ? 0 : baseShippingFee;

        // 주문 요약에 값 반영
        $("#total-items").text(totalItems);
        $("#total-price").text(totalPrice.toLocaleString() + "원");
        $("#discount-amount").text(totalDiscount.toLocaleString() + "원");
        $("#shipping-fee").text(shippingFee.toLocaleString() + "원");
        $("#final-total").text((totalPrice - totalDiscount + shippingFee).toLocaleString() + "원");
    }

    // 페이지 초기 로드 시 주문 요약 업데이트
    updateSummary();
//     bookDetail
    $(".cartAdd").click(function () {
        let selectedBookIds = [];
        let selectedBookQuantity = [];
        if ($(this).closest(".cart-Data").find(".cartAdd").attr("data-type") === "one") {
            selectedBookIds = $(this).closest(".cart-Data").find(".cart-checkbox").map(function () {
                return $(this).closest(".cart-Data").find(".cartAdd").attr("data-book-id");
            }).get();

            selectedBookQuantity = $(this).closest(".cart-Data").find(".cart-checkbox").map(function () {
                return $(this).closest(".cart-Data").find(".quantity").val();
            }).get();
        } else {
            selectedBookIds = $(".cart-checkbox:checked").map(function () {
                return $(this).closest(".cart-Data").find(".cartAdd").attr("data-book-id");
            }).get();

            selectedBookQuantity = $(".cart-checkbox:checked").map(function () {
                return $(this).closest(".cart-Data").find(".quantity").val();
            }).get();
        }

        if (selectedBookIds.length === 0) {
            alert("장바구니에 추가하실 상품을 선택해주세요.");
            return;
        }
        addCartItems(selectedBookIds, selectedBookQuantity);
    });

    // AJAX를 통한 선택된 항목 추가 함수
    function addCartItems(bookId, quantity) {
        $.ajax({
            url: '/cart/add',
            type: 'POST',
            traditional: true, // 배열을 쿼리 문자열로 전송하기 위해 추가
            data: {
                bookId: bookId,
                quantity: quantity
            },
            success: function (response) {
                if (response.success) {
                    alert("장바구니에 추가 되었습니다.");
                    location.reload();
                } else {
                    alert("장바구니에 추가 실패했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    }

//     paymentModal
// 결제 모달 버튼 클릭 시 초기화 및 계산
    $(".paymentModalBtn").click(function () {
        $(".modal-items").empty(); // 모달의 기존 항목 비우기

        const isAllSelected = $(this).data("type") === "all";
        const checkboxes = isAllSelected ? $(".cart-checkbox") : $(this).data("type") === "one" ? $(this).closest(".paymentModal-Data").find(".cart-checkbox") : $(".cart-checkbox:checked");

        let totalItems = 0;
        let totalOriginalPrice = 0;
        let totalDiscountedPrice = 0;
        let totalDiscount = 0;

        checkboxes.each(function () {
            const cardBody = $(this).closest(".paymentModal-Data");

            // 각 값들을 올바르게 가져오고 기본값 설정
            const title = cardBody.find(".title").text().trim() || "상품명 없음";
            const quantity = parseInt(cardBody.find(".quantity").val()) || 0;
            const price = parseInt(cardBody.find(".price").text().replace(/[^0-9]/g, "")) || 0;
            const discount = parseFloat(cardBody.find(".discount").text().replace(/[^0-9.]/g, "")) || 0;

            // 개별 상품의 총 가격 및 할인 적용 가격 계산
            const itemTotalPrice = price * quantity;
            const itemDiscount = Math.floor(itemTotalPrice * (discount / 100));
            const itemDiscountedPrice = Math.floor(itemTotalPrice - itemDiscount);

            // 총합 계산
            totalItems += quantity;
            totalOriginalPrice += itemTotalPrice;
            totalDiscountedPrice += itemDiscountedPrice;
            totalDiscount += itemDiscount;

            // 모달에 표시할 개별 상품 정보 HTML 생성
            const modalItem = `
            <div class="card col">
                <div class="row g-0">
                    <div class="col-md-4 pt-3">
                        <img src="https://via.placeholder.com/100" alt="임시 이미지">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <p>상품명: <span class="modalBookTitle">${title}</span></p>
                            <p>수량: <span class="modalQuantity">${quantity}</span></p>
                            <p>가격: <span class="modalTotalPrice">${itemTotalPrice.toLocaleString()}</span>원</p>
                            <p>할인율: <span class="modalDiscount">${discount}%</span></p>
                            <p>할인가: <span class="modalDiscountedPrice">${itemDiscountedPrice.toLocaleString()}</span>원</p>
                        </div>
                    </div>
                </div>
            </div>
        `;
            $(".modal-items").append(modalItem);
        });

        let shippingFee = totalDiscountedPrice >= shippingFeeThreshold ? 0 : baseShippingFee;

        // 사용 가능한 적립금 및 총 결제 금액 초기 계산 및 업데이트
        const availablePoints = parseInt($("#available-points").text().replace(/[^0-9]/g, "")) || 0;
        const usedPoints = parseInt($("#used-points").val()) || 0;
        updateOrderSummary(totalOriginalPrice, totalDiscount, shippingFee, totalDiscountedPrice, usedPoints);

        // 적립금 입력값 변경 시 총 결제 금액을 다시 계산
        $("#used-points").on("input", function () {
            // 입력된 값 가져오기
            let updatedUsedPoints = parseInt($(this).val()) || 0;

            // 최고값을 입력 필드의 max 속성에서 가져옴
            const maxPoints = parseInt($(this).attr("max"));

            // 최대값 이상 입력되지 않도록 제한 (즉시 적용)
            if (updatedUsedPoints > maxPoints) {
                updatedUsedPoints = maxPoints;
                $(this).val(updatedUsedPoints);
            }
        });

        // 적립금 입력 필드에서 포커스를 벗어났을 때
        $("#used-points").on("blur", function () {
            let updatedUsedPoints = parseInt($(this).val()) || 0;

            // 최고값을 입력 필드의 max 속성에서 가져옴
            const maxPoints = parseInt($(this).attr("max"));

            // 100 단위로만 입력될 수 있게끔 처리
            if (updatedUsedPoints % 100 !== 0) {
                updatedUsedPoints = Math.floor(updatedUsedPoints / 100) * 100;
            }

            // 최대값 이상 입력되지 않도록 제한
            if (updatedUsedPoints > maxPoints) {
                updatedUsedPoints = maxPoints;
            }

            // 수정된 값으로 input 값을 재설정
            $(this).val(updatedUsedPoints);

            // 총 결제 금액 업데이트
            updateOrderSummary(totalOriginalPrice, totalDiscount, shippingFee, totalDiscountedPrice, updatedUsedPoints);
        });

        // Enter 키 입력 시 폼 제출 방지
        $("#used-points").on("keydown", function (e) {
            if (e.key === "Enter") {
                e.preventDefault(); // Enter 키로 인한 폼 제출 방지
            }
        });

    });

    // 주문 요약 업데이트 함수
    function updateOrderSummary(totalOriginalPrice, totalDiscount, shippingFee, totalDiscountedPrice, usedPoints) {
        const finalTotal = totalDiscountedPrice + shippingFee - usedPoints;

        // 주문 확인 정보 업데이트
        $("#order-original-price").text(totalOriginalPrice.toLocaleString() + "원");
        $("#order-shipping-fee").text(shippingFee.toLocaleString() + "원");
        $("#order-final-total").text(finalTotal.toLocaleString() + "원");

        // 할인 금액 및 적립금 차감 내용 업데이트
        const deductionSummary = `
        <p>할인된 금액: -${totalDiscount.toLocaleString()}원</p>
        <p>적립금 사용: -${usedPoints.toLocaleString()}원</p>
    `;
        $("#order-deduction-summary").html(deductionSummary);
    }

    $(".point-all").click(function (e) {
        // 사용 가능한 적립금 텍스트에서 숫자 부분만 추출
        e.preventDefault();
        let point = $("#available-points").text().replace(/[^0-9]/g, ""); // '999,999원'에서 숫자만 추출
        point = parseInt(point, 10); // 문자열을 정수로 변환

        // 100 단위의 최대 사용 가능 포인트 계산
        let maxPoints = Math.floor(point / 100) * 100; // 100단위 내림 처리

        // 계산된 값을 input 창에 설정
        $("#used-points").val(maxPoints).trigger("blur");
    });

    $("#bank-transfer").click(function (e) {
        e.preventDefault();
        $(this).addClass("selected"); // 클릭된 버튼에 selected 클래스 추가
        $("#credit-card").removeClass("selected"); // 다른 버튼에서 selected 클래스 제거
        // 무통장 입금 버튼 클릭 시 신용 카드 버튼 흐리게 처리
        $("#credit-card").css("opacity", "0.5");
        $("#bank-transfer").css("opacity", "1"); // 클릭된 버튼은 원래대로 유지
    });

    $("#credit-card").click(function (e) {
        e.preventDefault();
        $(this).addClass("selected"); // 클릭된 버튼에 selected 클래스 추가
        $("#bank-transfer").removeClass("selected"); // 다른 버튼에서 selected 클래스 제거
        // 신용 카드 버튼 클릭 시 무통장 입금 버튼 흐리게 처리
        $("#bank-transfer").css("opacity", "0.5");
        $("#credit-card").css("opacity", "1"); // 클릭된 버튼은 원래대로 유지
    });

    // 오늘 날짜 가져오기
    let today = new Date();

    // 내일 날짜 계산
    let nextDay = new Date(today);
    nextDay.setDate(today.getDate() + 1);

    // 만약 내일이 토요일(6) 또는 일요일(0)이라면 월요일로 설정
    if (nextDay.getDay() === 6) {
        nextDay.setDate(nextDay.getDate() + 2); // 토요일인 경우 -> 월요일
    } else if (nextDay.getDay() === 0) {
        nextDay.setDate(nextDay.getDate() + 1); // 일요일인 경우 -> 월요일
    }

    // 날짜 형식 구성
    const year = nextDay.getFullYear();
    const month = nextDay.getMonth() + 1; // 월은 0부터 시작하므로 +1
    const date = nextDay.getDate();
    const dayNames = ["일", "월", "화", "수", "목", "금", "토"];
    const day = dayNames[nextDay.getDay()]; // 요일 이름 가져오기

    // 출고 예정일 업데이트
    const formattedDate = `${year}년 ${month}월 ${date}일 (${day})`;
    $("#expected-shipping-date").text(formattedDate);

    //     book
    $('#bookSortOptions').change(function () {
        // 선택된 <option> 요소의 data 속성 값을 가져옴
        const selectedOption = $('#bookSortOptions option:selected').attr("data-option");
        const selectedDirection = $('#bookSortOptions option:selected').attr("data-direction");

        // 방향 설정
        let direction;
        if (selectedDirection === "low" || selectedDirection === "new") {
            direction = "asc";
        } else if (selectedDirection === "high") {
            direction = "desc";
        }

        let urlParams = getUrlParams();
        // undefined 값을 빈 문자열로 대체
        const page = urlParams['page'] || "0";
        const ifkr = urlParams['ifkr'] || "";
        const category = urlParams['category'] || "";
        const subcategory = urlParams['subcategory'] || "";

        // URL 인코딩 적용
        const encodedSort = encodeURIComponent(selectedOption);
        const encodedDirection = encodeURIComponent(direction);
        const encodedCategory = encodeURIComponent(category);
        const encodedSubcategory = encodeURIComponent(subcategory);

        // 페이지 리로드 및 쿼리 파라미터에 sort 추가
        window.location.href = `/book?page=${page}&sort=${encodedSort}&direction=${encodedDirection}&ifkr=${ifkr}&category=${encodedCategory}&subcategory=${encodedSubcategory}`;
    });

    //     bookSerach

    $(".paymentModal-Data").each(function () {
        const $this = $(this);

        // 각 항목의 원래 가격과 할인율 요소 가져오기
        const priceElement = $this.find(".price");
        const discountElement = $this.find(".discount");
        const discountPriceElement = $this.find(".discountPrice");

        // 가격과 할인율을 숫자로 변환
        const price = parseInt(priceElement.text(), 10);
        const discount = parseInt(discountElement.text(), 10);

        // 천 단위 콤마 추가 함수
        function formatPrice(value) {
            return value.toLocaleString("ko-KR") + "원";
        }

        // 원래 가격에 천 단위 콤마 추가
        priceElement.text(formatPrice(price));

        // 할인된 가격 계산 및 표시
        const discountedPrice = price * (1 - discount / 100);
        discountPriceElement.text(formatPrice(Math.round(discountedPrice)));
    });

    $('#sortOptions').change(function () {
        // 선택된 <option> 요소의 data 속성 값을 가져옴
        const selectedOption = $('#sortOptions option:selected').attr("data-option");
        const selectedDirection = $('#sortOptions option:selected').attr("data-direction");

        // 방향 설정
        let direction;
        if (selectedDirection === "low" || selectedDirection === "new") {
            direction = "asc";
        } else if (selectedDirection === "high") {
            direction = "desc";
        }

        let urlParams = getUrlParams();
        // undefined 값을 빈 문자열로 대체
        const existingKeyword = urlParams['keyword'] || "";
        const existingVal = urlParams['val'] || "";
        const page = urlParams['page'] || "0";
        const ifkr = urlParams['ifkr'] || "";
        const category = urlParams['category'] || "";
        const subcategory = urlParams['subcategory'] || "";

        // URL 인코딩 적용
        const encodedKeyword = encodeURIComponent(existingKeyword);
        const encodedVal = encodeURIComponent(existingVal);
        const encodedSort = encodeURIComponent(selectedOption);
        const encodedDirection = encodeURIComponent(direction);
        const encodedCategory = encodeURIComponent(category);
        const encodedSubcategory = encodeURIComponent(subcategory);

        // 페이지 리로드 및 쿼리 파라미터에 sort 추가
        window.location.href = `/book/search?keyword=${encodedKeyword}&page=${page}&val=${encodedVal}&sort=${encodedSort}&direction=${encodedDirection}&ifkr=${ifkr}&category=${encodedCategory}&subcategory=${encodedSubcategory}`;
    });

    $('.research-btn').click(function () {
        const researchBox = $(this).closest('.research-data');
        const researchInput = researchBox.find('.research-input').val().trim();

        // 체크된 키워드들을 그룹으로 묶기
        let keywordGroups = [];
        let selectedKeywords = [];

        $(".research-checkbox:checked").each(function () {
            selectedKeywords.push($(this).attr("data-type"));
        });

        // 그룹화된 키워드를 배열에 추가
        if (selectedKeywords.length > 0) {
            keywordGroups.push("[" + selectedKeywords.join(",") + "]");
        }

        // URL의 기존 파라미터를 가져오기
        let urlParams = getUrlParams();
        const existingKeyword = urlParams['keyword'] || "";
        const existingVal = urlParams['val'] || "";
        const page = urlParams['page'] || "0";
        const sort = urlParams['sort'] || "";
        const direction = urlParams['direction'] || "";
        const ifkr = urlParams['ifkr'] || "";
        const category = urlParams['category'] || "";
        const subcategory = urlParams['subcategory'] || "";

        // 기존 그룹과 새로운 그룹을 결합하여 저장
        let newKeyword = existingKeyword ? `${existingKeyword},${keywordGroups.join(",")}` : keywordGroups.join(",");
        let newVal = existingVal ? `${existingVal},${researchInput}` : researchInput;

        // URL 인코딩 적용
        const encodedKeyword = encodeURIComponent(newKeyword);
        const encodedVal = encodeURIComponent(newVal);
        const encodedSort = encodeURIComponent(sort);
        const encodedDirection = encodeURIComponent(direction);
        const encodedCategory = encodeURIComponent(category);
        const encodedSubcategory = encodeURIComponent(subcategory);

        // 검색 페이지로 리다이렉트
        window.location.href = `/book/search?keyword=${encodedKeyword}&val=${encodedVal}&page=${page}&sort=${encodedSort}&direction=${encodedDirection}&ifkr=${ifkr}&category=${encodedCategory}&subcategory=${encodedSubcategory}`;
    });

// URL 파라미터를 객체 형태로 가져오는 함수
    function getUrlParams() {
        let params = {};
        let queryString = window.location.search.substring(1);
        let regex = /([^&=]+)=([^&]*)/g;
        let m;
        while (m = regex.exec(queryString)) {
            params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
        }
        return params;
    }

//     findIdPw

    // 버튼 클릭 시 폼 표시 토글
    $('#showFindIdBtn').on('click', function () {
        $('#findId').show();
        $('#initialForm, #idResult, #providerResult, #pwResultSuccess, #pwResultError').hide();
    });

    $('#showResetPwBtn').on('click', function () {
        $('#initialForm').show();
        $('#findId, #idResult, #providerResult, #pwResultSuccess, #pwResultError').hide();
    });

    // 비밀번호 재설정에서 이메일 인증
    $('#sendVerificationCodeForPw').on('click', function (event) {
        event.preventDefault(); // 기본 동작 방지
        const email = $('#emailForPw').val(); // 이메일 입력 필드 값 가져오기

        if (!email) {
            alert('이메일을 입력하세요.');
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/user/emailAuthentication',
            data: {email: email},
            success: function (response) {
                if (response.isEmail) {
                    alert('인증 코드가 이메일로 발송되었습니다.');
                    $('#verificationCodeGroupForPw').show(); // 인증번호 입력 필드 보이기
                } else {
                    alert('이메일이 일치하지 않습니다.');
                }
            },
            error: function () {
                alert('이메일 인증 요청 중 오류가 발생했습니다.');
            }
        });
    });

    $('#findIdBtn').on('click', function (event) {
        event.preventDefault(); // 폼 제출 기본 동작 방지
        const name = $("#nameForId").val(); // 이름 입력
        const tel = $("#telForId").val(); // 이메일 입력
        $.ajax({
            type: 'POST',
            url: '/user/findId',
            data: {
                name: name,
                tel: tel
            },
            success: function (response) {
                if (response.success === "true") {
                    $('#idResult').html(`당신의 아이디는 ${response.email} 입니다.`).show();
                    $('#providerResult').html(`당신의 가입경로는 ${response.provider} 입니다.`).show();
                } else {
                    alert('이름, 전화번호가 일치하지 않습니다.');
                }
            },
            error: function () {
                alert('이름, 전화번호가 일치하지 않습니다.');
            }
        });
    });

    // 비밀번호 재설정 요청
    $('#newPassword').on('click', function (event) {
        event.preventDefault(); // 폼 제출 기본 동작 방지
        const tel = $("#telForPw").val(); // 이름 입력
        const email = $("#emailForPw").val(); // 이메일 입력
        const verificationCode = $("#verificationCodeForPw").val(); // 인증 코드 입력

        $.ajax({
            type: 'POST',
            url: '/user/findPw',
            data: {tel: tel, email: email, verificationCode: verificationCode},
            success: function (response) {
                if (response.success === "true") {
                    $("#initialForm").hide();
                    $("#afterFind").show();
                    $("#hiddenEmail").val(response.email);
                } else {
                    $("#pwResultError").html(response.error).show();
                }
            },
            error: function () {
                alert('비밀번호 재설정 중 오류가 발생했습니다.');
            }
        });
    });

    // 비밀번호 업데이트 요청
    $('#updateBtn').on('click', function (event) {
        event.preventDefault(); // 폼 제출 기본 동작 방지

        const email = $("#hiddenEmail").val(); // 아이디 입력
        const password = $("#newPw").val(); // 이메일 입력
        const confirmPassword = $('#confirmPassword').val();

        if (password !== confirmPassword) {
            $('#pwConfirmError').html('비밀번호가 일치하지 않습니다.').show();
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/user/newPw',
            data: {email: email, password: password},
            success: function (response) {
                if (response.success === "true") {
                    $('#pwResultSuccess').html('비밀번호 변경 되었습니다.').show();
                    if (confirm("비밀번호가 변경 되었습니다. 로그인 화면으로 이동할까요?")) {
                        location.replace("/login");
                    }
                } else {
                    $('#pwConfirmError').html('비밀번호 재설정 중 오류가 발생했습니다.').show();
                }
            },
            error: function () {
                alert('비밀번호 재설정 중 오류가 발생했습니다.');
            }
        });
    });
});