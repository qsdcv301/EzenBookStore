$(document).ready(function () {

    const IMP = window.IMP;
    IMP.init("imp27564405");

    // 모달 z-index 관리
    $('.modal').on('show.bs.modal', function (event) {
        var zIndex = 1040 + (10 * $('.modal:visible').length);
        $(this).css('z-index', zIndex);
        setTimeout(function () {
            $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
        }, 0);
    });

    $("#searchAddress").click(function () {
        new daum.Postcode({
            oncomplete: function (data) {
                let addr = "";

                if (data.userSelectedType === "R") {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                $("#addr").val(addr);
                $("#addrextra").focus();
            },
        }).open();
    });

    $('.loginCheck').click(function (e) {
        e.preventDefault();
        if (confirm("로그인이 필요합니다. 로그인 페이지로 이동할까요?")) {
            location.replace("/login");
        }
    });

    $(window).on('load', function () {
        $('.price').each(function () {
            let price = parseInt($(this).text().replace(/[^0-9]/g, "")) || 0;
            let formattedPrice = price.toLocaleString("ko-KR") + "원";
            $(this).text(formattedPrice);
        });
    });

    //header

    $('.dropdown-arrow').on('click', function(event) {
        event.preventDefault();
        const target = $(this).attr('data-target');
        $(target).collapse('toggle');

        // 드롭다운 메뉴가 닫히지 않도록 이벤트 전파 방지
        event.stopPropagation();
    });

    $(".searchBtn").click(function (e) {
        e.preventDefault();
        const searchSelect = $(".searchSelect").select().val();
        const searchInput = $(".searchInput").val().trim();
        let encodedKeyword = encodeURIComponent("[title,author,isbn,publisher]");
        switch (searchSelect){
            case "0":
                 encodedKeyword = encodeURIComponent("[title,author,isbn,publisher]");
                window.location.href = `/book/search?keyword=${encodedKeyword}&val=${searchInput}`;
                break;
            case "1":
                 encodedKeyword = encodeURIComponent("[title]");
                window.location.href = `/book/search?keyword=${encodedKeyword}&val=${searchInput}`;
                break;
            case "2":
                 encodedKeyword = encodeURIComponent("[author]");
                window.location.href = `/book/search?keyword=${encodedKeyword}&val=${searchInput}`;
                break;
            case "3":
                 encodedKeyword = encodeURIComponent("isbn]");
                window.location.href = `/book/search?keyword=${encodedKeyword}&val=${searchInput}`;
                break;
            case "4":
                 encodedKeyword = encodeURIComponent("[publisher]");
                window.location.href = `/book/search?keyword=${encodedKeyword}&val=${searchInput}`;
                break;
            default :
                 encodedKeyword = encodeURIComponent("[title,author,isbn,publisher]");
                window.location.href = `/book/search?keyword=${encodedKeyword}&val=${searchInput}`;
                break;
        }
    });

    // signup
    let currentStep = 1;
    const totalSteps = 3;

    // 현재 단계 표시 함수
    function showStep(step) {
        $('.step').each(function (index) {
            if (index + 1 === step) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        });

        // 진행 바 업데이트
        $('.progress-bar').css('width', `${(step / totalSteps) * 100}%`);
        $('.progress-bar').text(`${step}/${totalSteps}`);
    }

    // "다음" 버튼 클릭 시 다음 단계로 이동
    $('#nextBtn1').on('click', function () {
        if (validateStep1()) {
            currentStep++;
            showStep(currentStep);
            alert("약관 동의가 완료되었습니다.");
        }
    });

    $('#nextBtn2').on('click', function () {
        if (validateStep2()) {
            const userIdCehck = $(this).attr("data-check");
            if (userIdCehck === 0) {
                alert("아이디 중복검사를 진행 후 회원가입이 가능합니다.");
                return;
            }
            const form = $('#step2');
            const userId = form.find('#userId').val();
            const name = form.find('#userName').val();
            const password = form.find('#userPwCheck').val();
            const tel = form.find('#userTel').val();
            const birthday = form.find('#userBirth').val();
            const addr = form.find('#addr').val();
            const addrextra = form.find('#addrextra').val();
            $.ajax({
                type: 'POST',
                url: `/signup`,
                data: {
                    email: userId,
                    provider: "ezen",
                    name: name,
                    password: password,
                    tel: tel,
                    addr: addr,
                    addrextra: addrextra,
                    birthdayString: birthday,
                },
                success: function (response) {
                    if (response.success) {
                        alert("회원 가입에 성공했습니다.");
                        currentStep++;
                        showStep(currentStep);
                    } else {
                        alert("회원 가입에 실패 했습니다.");
                    }
                },
                error: function () {
                    alert("서버 오류가 발생했습니다.");
                }
            });
        }
    });

    // "이전" 버튼 클릭 시 이전 단계로 이동
    $('#prevBtn2').on('click', function () {
        currentStep--;
        showStep(currentStep);
    });

    // 초기 단계 표시
    showStep(currentStep);

    // "모든 약관 동의" 체크박스를 클릭했을 때
    $('#allAgree').on('change', function () {
        $('.agreement-checkbox').prop('checked', $('#allAgree').prop('checked'));
    });

    // 개별 약관 체크박스를 클릭했을 때 "모든 약관 동의" 상태 업데이트
    $('.agreement-checkbox').on('change', function () {
        $('#allAgree').prop('checked', $('.agreement-checkbox').filter(':checked').length === $('.agreement-checkbox').length);
    });

    // Step 1: 약관 동의 유효성 검사
    function validateStep1() {
        let isValid = true;
        let errorMessage = "";

        $('.agreement-checkbox').each(function () {
            if ($(this).prop('required') && !$(this).prop('checked')) {
                isValid = false;
                $(this).addClass('is-invalid');
                errorMessage += $(this).next().text() + " 에 동의해주세요.\n";
            } else {
                $(this).removeClass('is-invalid');
            }
        });

        if (!isValid) {
            alert(errorMessage);
        }

        return isValid;
    }

    // 이메일 실시간 유효성 검사
    $('#userId').on('input', function () {
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        const $feedbackMessage = $('#feedbackMessage');
        if (!emailPattern.test($('#userId').val())) {
            $(this).addClass('is-invalid').removeClass('is-valid');
            $feedbackMessage.text('올바른 이메일 형식을 입력해주세요.');
            $feedbackMessage.css("color", "#dc3545");
        } else {
            $(this).removeClass('is-invalid').addClass('is-valid');
            $feedbackMessage.text('사용 가능한 아이디입니다.');

            $feedbackMessage.css("color", "#28a745");
        }
        $feedbackMessage.css("display", "block");
    });

    // 비밀번호 실시간 유효성 검사
    $('#userPw').on('input', function () {
        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
        if (!passwordPattern.test($('#userPw').val())) {
            $('#userPw').addClass('is-invalid').removeClass('is-valid');
        } else {
            $('#userPw').removeClass('is-invalid').addClass('is-valid');
        }

        // 비밀번호 확인도 함께 실시간으로 검사
        validatePasswordCheck();
    });

    // 비밀번호 확인 실시간 검사
    $('#userPwCheck').on('input', validatePasswordCheck);

    function validatePasswordCheck() {
        if ($('#userPw').val() !== $('#userPwCheck').val()) {
            $('#userPwCheck').addClass('is-invalid').removeClass('is-valid');
        } else {
            $('#userPwCheck').removeClass('is-invalid').addClass('is-valid');
        }
    }

    // 전화번호 실시간 유효성 검사 및 하이픈 추가 기능
    $('#userTel').on('input', function () {
        let phoneValue = $('#userTel').val().replace(/[^0-9]/g, ''); // 숫자 이외의 문자는 제거

        if (phoneValue.length > 3 && phoneValue.length <= 7) {
            phoneValue = phoneValue.replace(/(\d{3})(\d{1,4})/, '$1-$2');
        } else if (phoneValue.length > 7) {
            phoneValue = phoneValue.replace(/(\d{3})(\d{4})(\d{1,4})/, '$1-$2-$3');
        }

        $('#userTel').val(phoneValue); // 포맷팅된 값을 다시 입력 필드에 넣음

        const phonePattern = /^01[016789]-\d{3,4}-\d{4}$/; // 하이픈 포함된 형식으로 변경
        if (!phonePattern.test(phoneValue)) {
            $('#userTel').addClass('is-invalid').removeClass('is-valid');
        } else {
            $('#userTel').removeClass('is-invalid').addClass('is-valid');
        }
    });

    // Step 2: 개인정보 입력 유효성 검사 함수 (다음 버튼 클릭 시)
    function validateStep2() {
        let isValid = true;
        let errorMessage = "";

        if (!$('#userId').hasClass('is-valid')) {
            isValid = false;
            errorMessage += "올바른 이메일 주소를 입력해주세요.\n";
        }
        if (!$('#userPw').hasClass('is-valid')) {
            isValid = false;
            errorMessage += "올바른 비밀번호를 입력해주세요.\n";
        }
        if (!$('#userPwCheck').hasClass('is-valid')) {
            isValid = false;
            errorMessage += "비밀번호가 일치하지 않습니다.\n";
        }
        if (!$('#userTel').hasClass('is-valid')) {
            isValid = false;
            errorMessage += "올바른 전화번호를 입력해주세요.\n";
        }

        if (!isValid) {
            alert(errorMessage);
        }

        return isValid;
    }

    $('#checkDuplicateBtn').click(function (e) {
        e.preventDefault();
        const form = $('#step2');
        const userId = form.find('#userId').val();
        $.ajax({
            type: 'POST',
            url: `/duplication/${userId}`,
            success: function (response) {
                if (response.success) {
                    alert("사용 불가능한 아이디입니다.");
                    $('#nextBtn2').attr("data-check", "0");
                    $('#checkDuplicateBtn').removeClass('btn-primary').addClass('btn-warning');
                } else {
                    alert("사용 가능한 아이디입니다.");
                    $('#nextBtn2').attr("data-check", "1");
                    $('#checkDuplicateBtn').removeClass('btn-warning').addClass('btn-success');
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    // customerService
    $('#userQnASelect').change(function () {
        const sort = $('#userQnASelect option:selected').val();

        // 현재 URL에서 qnaPage 값을 추출
        const urlParams = new URLSearchParams(window.location.search);
        const qnaPage = urlParams.has('qnaPage') ? urlParams.get('qnaPage') : 0;

        // 현재 URL 경로 확인
        const currentPath = window.location.pathname;
        const basePath = currentPath.includes('/user/info') ? '/user/info' : '/customerService';

        // 새로운 URL로 리디렉션
        window.location.href = `${basePath}?qnaPage=${qnaPage}&sort=${sort}`;
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
                    // 이미지 경로 설정
                    if (response.imagePath) {
                        // 이미지가 존재할 때: src를 설정하고, 요소를 보여줍니다.
                        $("#inquiryContentImage").attr("src", response.imagePath);
                        $(".inquiryImage").show();
                    } else {
                        // 이미지가 없을 때: src를 비우고, 요소를 숨깁니다.
                        $("#inquiryContentImage").attr("src", "")
                        $(".inquiryImage").hide();
                    }

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
        const fileInputs = $(this).closest('.questionModal').find('#customFile')[0].files;
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

        // FormData 객체 생성 및 데이터 추가
        const formData = new FormData();
        formData.append('email', email);
        formData.append('category', category);
        formData.append('title', title);
        formData.append('question', question);
        // 파일 배열을 반복하여 추가
        for (let i = 0; i < fileInputs.length; i++) {
            formData.append('files', fileInputs[i]);
        }

        $.ajax({
            url: '/qna/add',
            type: 'POST',
            processData: false,
            contentType: false,
            data: formData,
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
    });

    $('#customFile').on('change', function () {
        // 파일 이름을 레이블에 표시
        const fileName = $(this).val().split('\\').pop();
        $(this).next('.custom-file-label').html(fileName);
    });

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
            const cartId = cardBody.find(".cartId").val() || "";
            const title = cardBody.find(".title").text().trim() || "상품명 없음";
            const quantity = parseInt(cardBody.find(".quantity").val()) || 0;
            const price = parseInt(cardBody.find(".price").text().replace(/[^0-9]/g, "")) || 0;
            const discount = parseFloat(cardBody.find(".discount").text().replace(/[^0-9.]/g, "")) || 0;
            const bookImage = cardBody.find(".bookImage").attr("src");
            const bookImageAlt = cardBody.find(".bookImage").attr("alt");

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
            <div class="card col paymentItems">
                <div class="row g-0">
                    <div class="col-md-5 pt-3 d-flex align-items-center justify-content-center">
                        <img src="${bookImage}" alt="${bookImageAlt}" style="width: 100px;height: 150px;object-fit: cover">
                    </div>
                    <div class="col-md-7">
                        <div class="card-body pl-0">
                            <input type="hidden" class="modalCartId" value="${cartId}">
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

    $(".confirmPurchase").click(function (e) {
        e.preventDefault();
        const name = $(".modalBookTitle").eq(0).text();
        const quantity = $(".modalQuantity").eq(0).text();
        let quantityList = [];
        let titleList = [];
        let totalQuantity = 0;
        let cartIdList = [];
        $(".modalQuantity").each(function () {
            totalQuantity += parseInt($(this).text().replace(/[^0-9]/g, ""), 10) || 0;
            quantityList.push($(this).text().replace(/[^0-9]/g, ""));
        });

        $(".modalBookTitle").each(function () {
            titleList.push($(this).text());
        });
        $(".modalCartId").each(function () {
            const value = $(this).val();
            if (value) {
                cartIdList.push(value);
            }
        });
        console.log(cartIdList);
        totalQuantity -= quantity;
        const userAddr = $(".payment-addr").val();
        const userAddrextra = $(".payment-addrextra").val();
        const amount = $("#order-final-total").text().replace(/[^0-9]/g, "") || 0;
        const userEmail = $(".payment-email").val();
        const userName = $(".payment-name").val();
        const userTel = $(".payment-tel").val();
        const paymentCode = Date.now();
        IMP.request_pay(
            {
                pg: "html5_inicis",
                pay_method: "card",
                merchant_uid: paymentCode,
                name: name + ` ${quantity}개 외 ` + totalQuantity + "개 상품",
                amount: amount,
                buyer_email: userEmail,
                buyer_name: userName,
                buyer_tel: userTel,
            },
            function (response) {
                if (response.success) {
                    $.ajax({
                        url: '/order/payment',
                        type: 'POST',
                        data: {
                            paymentCode: paymentCode,
                            titleList: titleList,
                            quantityList: quantityList,
                            cartIdList: cartIdList,
                            amount: amount,
                            userName: userName,
                            addr: userAddr,
                            addrextra: userAddrextra,
                            tel: userTel,
                        },
                        success: function (response) {
                            if (response.success) {
                                alert("결제가 완료되었습니다.");
                                location.reload();
                            } else {
                                alert("결제에 실패했습니다.");
                                location.reload();
                            }
                        },
                        error: function () {
                            alert("서버 오류가 발생했습니다.");
                        }
                    });
                } else {
                    alert("결제에 실패했습니다.");
                    location.reload();
                }
            },
        );
    });

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

    $('#verifyCurrentPassword').on('click', function (event) {
        event.preventDefault();
        const currentPassword = $('#currentPassword').val();
        const confirmCurrentPassword = $('#confirmCurrentPassword').val();

        if (currentPassword === confirmCurrentPassword) {
            $('#newPasswordFields').show();
            $(this).hide();
            $('#changePassword').show();
        } else {
            alert("현재 비밀번호와 현재 비밀번호 확인의 값이 일치하지 않습니다.");
        }
    });

    $('#changePassword').on('click', function (event) {
        event.preventDefault();
        const newPassword = $('#newPassword').val();
        const confirmNewPassword = $('#confirmNewPassword').val();
        const confirmCurrentPasswordEmail = $('#confirmCurrentPasswordEmail').val();

        if (newPassword !== confirmNewPassword) {
            alert("현재 비밀번호와 현재 비밀번호 확인의 값이 일치하지 않습니다.");
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/user/newPw',
            data: {email: confirmCurrentPasswordEmail, password: newPassword},
            success: function (response) {
                if (response.success === "true") {
                    $('#pwResultSuccess').html('비밀번호 변경 되었습니다.').show();
                    if (confirm("비밀번호가 변경 되었습니다. 보안을 위해 로그아웃합니다.")) {
                        location.replace("/logout");
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

    //     info
    const urlParams = new URLSearchParams(window.location.search);

    if (urlParams.get('first') === 'true') {
        alert("간편 로그인 회원입니다.\n원활한 이용을 위해 개인정보를 입력해주세요.");
    }

    $('.userUpdate').on('click', function (event) {
        event.preventDefault(); // 폼 제출 기본 동작 방지
        const userId = $(this).attr('data-id');
        const myInfo = $(this).closest('.myInfo');
        const tel = myInfo.find('#tel').val();
        const birthday = myInfo.find('#birthdate').val();
        const addr = myInfo.find('#addr').val();
        const addrextra = myInfo.find('#addrextra').val();
        if (tel.trim() === '' || addr.trim() === '' || addrextra === '' || birthday === '') {
            alert("수정 정보에 빈값은 허용되지 않습니다.");
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/user/update',
            traditional: true,
            data: {
                userId: [userId],
                tel: [tel],
                birthday: [birthday],
                addr: [addr],
                addrextra: [addrextra],
            },
            success: function (response) {
                if (response.success) {
                    alert("회원 정보가 성공적으로 수정 되었습니다.")
                    location.replace("/user/info");
                } else {
                    alert("회원 정보 수정중 오류가 발생했습니다.");
                    location.reload();
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    $('.userDelete').on('click', function (event) {
        event.preventDefault(); // 폼 제출 기본 동작 방지
        const userId = $(this).attr('data-id');
        $.ajax({
            type: 'POST',
            url: '/user/delete',
            data: {userId: [userId]},
            success: function (response) {
                if (response.success) {
                    alert("회원탈퇴 되었습니다. 로그인창으로 이동합니다.")
                    location.replace("/login");
                } else {
                    alert("회원탈퇴 중 오류가 발생했습니다..");
                    location.reload();
                }
            },
            error: function () {
                alert("회원탈퇴 중 오류가 발생했습니다..");
            }
        });
    });

    // orderInfo

    $('.searchKeywordBtn').click(function (e) {
        e.preventDefault();
        const keyword = $('.searchKeyword').val();

        // 새로운 URL로 리디렉션
        window.location.href = `/user/info?keyword=${keyword}&orderPage=0`;
    });

    // 날짜 범위 선택 변경 시 이벤트
    $('#dateRange').change(function () {
        $('#customDateRange').toggle($(this).val() === 'custom');
    });

    // 필터 적용 버튼 클릭 시 이벤트
    $('#applyFilters').click(function () {
        let dateRange = $('#dateRange').val();
        const deliveryStatus = $('#deliveryStatus').val();
        const orderStatus = $('#orderStatus').val();
        let startDate = $('#startDate').val();
        let endDate = $('#endDate').val();
        if (dateRange) {
            if (dateRange === 'custom') {
                startDate = $('#startDate').val();
                endDate = $('#endDate').val();
                if (startDate && endDate) {
                    dateRange = '';
                }
            }
        }
        let deliveryStatusText = '';
        if (deliveryStatus && deliveryStatus !== '') {
            deliveryStatusText = $('#deliveryStatus option:selected').val();
        }
        let orderStatusText = '';
        if (orderStatus && orderStatus !== '') {
            orderStatusText = $('#orderStatus option:selected').val();
        }
        // 현재 URL에서 값을 추출
        const urlParams = new URLSearchParams(window.location.search);
        const keyword = urlParams.has('keyword') ? urlParams.get('keyword') : '';
        const direction = urlParams.has('direction') ? urlParams.get('direction') : '';
        window.location.href = `/user/info?orderPage=0&keyword=${keyword}&direction=${direction}&deliveryStatus=${deliveryStatusText}&orderStatus=${orderStatusText}&dateRange=${dateRange}&startDate=${startDate}&endDate=${endDate}`;

    });

    // 필터 초기화 버튼 클릭 시 이벤트
    $('#resetFilters').click(function () {
        $('#dateRange, #deliveryStatus, #orderStatus').val('');
        $('#startDate, #endDate').val('');
        $('#customDateRange').hide();
        $('#appliedFilters').empty();
        window.location.href = `/user/info?orderPage=0`;
    });

    // 필터 태그 제거 기능
    $('#appliedFilters').on('click', '.badge', function (e) {
        const filterType = $(this).find('span').text().split(':')[0].trim(); // 필터의 타입 추출
        let urlParams = new URLSearchParams(window.location.search);

        // 필터에 따라 URL 파라미터에서 제거할 필터 결정
        if (filterType === '기간') {
            if (urlParams.has('startDate') && urlParams.has('endDate')) {
                urlParams.delete('startDate');
                urlParams.delete('endDate');
            } else if (urlParams.has('dateRange')) {
                urlParams.delete('dateRange');
            }
        } else if (filterType === '배송 상태') {
            urlParams.delete('deliveryStatus');
        } else if (filterType === '주문 상태') {
            urlParams.delete('orderStatus');
        }

        // 필터 제거 후 새 URL로 리디렉션
        window.location.href = `/user/info?${urlParams.toString()}`;
    });

    // 주문 상세 정보 모달 요청 처리 orderDetailModal
    $('.orderDetailBtn').click(function () {
        const ordersId = $(this).attr('data-id');
        updateMainModalData(ordersId);
    });

// 메인 모달 데이터 업데이트 함수 분리
    function updateMainModalData(ordersId) {
        // AJAX 요청을 통해 상세 데이터 가져오기
        $.ajax({
            url: `/order/${ordersId}`,
            type: 'POST',
            success: function (response) {
                if (response.success === "true") {
                    // 상품 정보 설정
                    const orderDetailTableBody = $('#orderDetailTableBody');
                    orderDetailTableBody.empty(); // 기존 데이터 제거
                    let orderStatusCheck = 0;
                    for (let i = 0; i < response.titleList.length; i++) {
                        if (parseInt(response.orderItemListStatus[i]) !== 1) {
                            orderStatusCheck = 1;
                        }
                        const orderSuccessBtnDisabled = parseInt(response.orderItemListStatus[i]) !== 2 ? 'disabled' : '';
                        const orderExchangeNreturnBtnDisabled = parseInt(response.orderItemListStatus[i]) !== 2 ? 'disabled' : '';
                        const reviewBtnDisabled = parseInt(response.orderItemListStatus[i]) !== 3 ? 'disabled' : '';
                        const row = `
                    <tr class="text-center orderItemsTable">
                        <td class="align-middle">
                            <img src="${response.imageList[i] || "https://via.placeholder.com/100"}" alt="책 표지" width="60" class="orderImage">
                        </td>
                        <td class="align-middle text-left">
                            <p class="mb-1">
                                <strong class="d-inline-block">제목:</strong>
                                <span class="text-truncate d-inline-block orderTitle" style="max-width: 120px; vertical-align: top;">${response.titleList[i]}</span>
                            </p>
                            <p class="mb-1">
                                <strong class="d-inline-block">저자:</strong>
                                <span class="text-truncate d-inline-block orderAuthor" style="max-width: 120px; vertical-align: top;">${response.authorList[i]}</span>
                            </p>
                            <p class="mb-0">
                                <strong class="d-inline-block">출판사:</strong>
                                <span class="text-truncate d-inline-block orderPublisher" style="max-width: 120px; vertical-align: top;">${response.publisherList[i]}</span>
                            </p>
                        </td>
                        <td class="align-middle">1</td>
                        <td class="align-middle">${response.priceList[i]}원</td>
                        <td class="align-middle">
                            <button class="btn btn-sm btn-warning text-white orderExchangeNreturnBtn" data-toggle="modal" data-target="#exchangeNreturn" ${orderExchangeNreturnBtnDisabled}>교환/반품</button>
                        </td>
                        <td class="align-middle">
                            <button class="btn btn-sm btn-success orderSuccessBtn" data-toggle="modal" data-target="#orderConfirmation" data-id="${response.orderItemList[i]}" ${orderSuccessBtnDisabled}>구매확정</button>
                        </td>
                        <td class="align-middle">
                            <button class="btn btn-sm btn-primary reviewBtn" data-toggle="modal" data-target="#reviewModal" data-id="${response.orderItemList[i]}"  ${reviewBtnDisabled}>리뷰작성</button>
                        </td>
                    </tr>
                `;
                        orderDetailTableBody.append(row);
                    }

                    // 주문 정보 설정
                    $('.orderId').text(ordersId);
                    $('.orderDate').text(response.orderDate || "");
                    $('.deliveryStatus').text(response.deliveryStatus || "");

                    // 배송 정보 설정
                    $('.deliveryAddr').text(response.deliveryAddr || "");
                    $('.deliveryAddrextra').text(response.deliveryAddrextra || "");
                    $('.deliveryTracking').text(response.deliveryTracking || "");

                    // 결제 정보 설정
                    $('.paymentStatus').text(response.paymentStatus || "");
                    $('.paymentAmount').text(response.paymentAmount || "");

                    if (orderStatusCheck === 0) {
                        $('.orderCancel').show();
                    } else {
                        $('.orderCancel').hide();
                    }

                } else {
                    alert("상품 상세보기 불러오기를 실패했습니다.");
                }
            },
            error: function (xhr, status, error) {
                console.error("서버 오류가 발생했습니다.");
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    }

    // 주문 취소 요청
    $(document).on('click', '.orderCancel', function (e) {
        const orderId = $(".orderId").text();
        $.ajax({
            type: 'POST',
            url: '/order/orderCancel',
            data: {orderId: orderId},
            success: function (response) {
                if (response.success) {
                    alert("주문 취소 요청을 했습니다.");
                    updateMainModalData(orderId);
                } else {
                    alert("주문 취소 요청중 오류가 발생했습니다.");
                    updateMainModalData(orderId);
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    // 반품/교환 요청 모달
    $(document).on('click', '.orderExchangeNreturnBtn', function (e) {
        const bookTitle = $(this).closest('.orderItemsTable').find('.orderTitle').text();
        $('#exchangeReturnTitle').val(bookTitle);
    });

    $(document).on('click', '#submitExchangeReturn', function (e) {
        e.preventDefault();
        const form = $('#exchangeReturnForm'); // 폼을 직접 선택
        const category = form.find('#exchangeReturnCategory option:selected').val(); // 선택된 값
        const question = form.find('#exchangeReturnReason').val(); // 사유
        const file = form.find('.exchangeReturnFile')[0]?.files[0]; // 파일 선택
        if (category === "0") {
            alert("교환/환불 유형을 선택해주세요.");
            return;
        }
        if (question.trim() === "") {
            alert("교환/환불 사유 내용을 작성 해주세요.");
            return;
        }
        if (file == null) {
            alert("교환/환불 사유 사진을 포함시켜주세요.");
            return;
        }

        // FormData 객체 생성 및 데이터 추가
        const formData = new FormData();
        formData.append('category', category);
        formData.append('question', question);
        formData.append('file', file);

        $.ajax({
            url: '/er/add',
            type: 'POST',
            processData: false,
            contentType: false,
            data: formData,
            success: function (response) {
                if (response.success) {
                    alert("교환/환불 신청을 했습니다.");
                    location.reload();
                } else {
                    alert("교환/환불 신청에 실패했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    $(document).on('change', '#exchangeReturnFile', function (e) {
        // 파일 이름을 레이블에 표시
        const fileName = $(this).val().split('\\').pop();
        $(this).next('.custom-file-label').html(fileName);
    });

    // 파일 유효성 검사 및 파일명 표시 (교환/반품 모달)
    $(document).on('change', '#imageFile', function (e) {
        const file = this.files[0];
        const fileType = file.type;
        const validTypes = ['image/jpeg', 'image/png', 'image/gif'];
        if (!validTypes.includes(fileType)) {
            alert('JPG, PNG, GIF 형식의 이미지만 업로드 가능합니다.');
            $(this).val('');
        } else {
            $(this).next('.custom-file-label').html(file.name);
        }
    });

    // 구매 확정 모달
    $(document).on('click', '.orderSuccessBtn', function () {
        const orderItemId = $(this).attr('data-id');
        // AJAX 요청을 통해 상세 데이터 가져오기
        $.ajax({
            url: `/order/success/${orderItemId}`,
            type: 'POST',
            success: function (response) {
                if (response.success === "true") {
                    $("#confirmProductTitle").text(response.orderItemTitle);
                    $("#confirmProductAuthor").text(response.orderItemAuthor);
                    $("#confirmProductPublisher").text(response.orderItemPublisher);
                    $("#confirmProductPrice").text(parseInt(response.orderItemPrice).toLocaleString('ko-KR') + '원');
                    $("#confirmPurchaseBtn").attr("data-id", response.orderItemId);
                    const orderItemStock = parseFloat(response.orderItemStock);
                    const orderItemPrice = parseFloat(response.orderItemPrice);
                    const userGrade = response.userGrade;
                    let userGradePoint = 0;
                    let userGradePercent = 0;

                    switch (userGrade) {
                        case "1" :
                            userGradePoint = 0.01;
                            userGradePercent = 1;
                            break;
                        case "2" :
                            userGradePoint = 0.03;
                            userGradePercent = 3;
                            break;
                        case "3" :
                            userGradePoint = 0.05;
                            userGradePercent = 5;
                            break;
                        default:
                            userGradePoint = 0;
                            userGradePercent = 0;
                            break;
                    }

                    const savepoint = Math.floor((orderItemPrice * orderItemStock) * userGradePoint);
                    const reviewPoint = Math.floor((orderItemPrice * orderItemStock) * 0.005);
                    $("#savePoint").text(savepoint);
                    $("#userGradePoint").text(userGradePercent);
                    $("#reviewPoint").text(reviewPoint);

                    if (response.imagePath) {
                        $(".orderSuccessImg").attr("src", response.imagePath);
                        $(".orderSuccessImg").attr("alt", response.title + '사진');
                    } else {
                        $(".orderSuccessImg").attr("src", "https://via.placeholder.com/100");
                        $(".orderSuccessImg").attr("alt", '임시 데이터 사진');
                    }
                } else {
                    alert("상품 상세보기 불러오기를 실패했습니다.");
                }
            },
            error: function (xhr, status, error) {
                console.error("서버 오류가 발생했습니다.");
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    });

    $(document).on('click', '#confirmPurchaseBtn', function (event) {
        event.preventDefault();
        const orderItemId = $(this).attr('data-id');
        const userId = $(this).attr('data-userId');
        const point = $("#savePoint").text();

        $.ajax({
            type: 'POST',
            url: '/order/success',
            data: {
                orderItemId: orderItemId,
                userId: userId,
                point: point,
            },
            success: function (response) {
                if (response.success) {
                    alert("구매를 확정했습니다.");
                    // 메인 모달 데이터 갱신
                    const ordersId = $('.orderId').text(); // 현재 모달에 표시된 주문 ID 가져오기
                    updateMainModalData(ordersId);
                    // 모달 닫기
                    $('#orderConfirmation').modal('hide');
                } else {
                    alert("구매 확정중 오류가 발생했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    // 리뷰 파일 유효성 검사 및 파일명 표시
    $('#reviewFile').on('change', function () {
        const file = this.files[0];
        const fileType = file.type;
        const validTypes = ['image/jpeg', 'image/png', 'image/gif'];
        if (!validTypes.includes(fileType)) {
            alert('JPG, PNG, GIF 형식의 이미지만 업로드 가능합니다.');
            $(this).val('');
        } else {
            $(this).next('.custom-file-label').html(file.name);
        }
    });

    // 별점 시스템
    $('#ratingStars i').on('click', function () {
        const rating = $(this).data('rating');
        $('#ratingValue').val(rating);
        $('#ratingStars i').removeClass('bi-star-fill').addClass('bi-star');
        $(this).prevAll().addBack().removeClass('bi-star').addClass('bi-star-fill');
    });

    // 리뷰 부분
    $(document).on('click', '.reviewBtn', function () {
        const orderItemId = $(this).attr('data-id');
        // AJAX 요청을 통해 상세 데이터 가져오기
        $.ajax({
            url: `/order/success/${orderItemId}`,
            type: 'POST',
            success: function (response) {
                if (response.success === "true") {
                    $("#reviewProductTitle").text(response.orderItemTitle);
                    $("#reviewProductAuthor").text(response.orderItemAuthor);
                    $("#reviewProductPublisher").text(response.orderItemPublisher);
                    $("#reviewProductPrice").text(parseInt(response.orderItemPrice).toLocaleString('ko-KR') + '원');
                    $("#submitReview").attr("data-id", response.orderItemId);

                    if (response.imagePath) {
                        $(".orderReviewImg").attr("src", response.imagePath);
                        $(".orderReviewImg").attr("alt", response.title + '사진');
                    } else {
                        $(".orderReviewImg").attr("src", "https://via.placeholder.com/100");
                        $(".orderReviewImg").attr("alt", '임시 데이터 사진');
                    }
                } else {
                    alert("리뷰에 필요한 데이터를 가져오는데 실패했습니다.");
                }
            },
            error: function (xhr, status, error) {
                console.error("서버 오류가 발생했습니다.");
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    });

    // 리뷰 제출
    $('#submitReview').on('click', function () {
        const rating = $('#ratingValue').val();
        const reviewTitle = $('#reviewTitle').val();
        const reviewText = $('#reviewText').val();
        const orderItemId = $("#submitReview").attr("data-id");
        const fileInputs = $('#reviewFile')[0].files;

        if (!rating) {
            alert('별점을 선택해주세요.');
            return;
        }
        if (!reviewTitle) {
            alert('리뷰 제목을 입력해주세요.');
            return;
        }
        if (!reviewText) {
            alert('리뷰 내용을 입력해주세요.');
            return;
        }

        // FormData 객체 생성 및 데이터 추가
        const formData = new FormData();
        formData.append('rating', rating);
        formData.append('title', reviewTitle);
        formData.append('comment', reviewText);
        formData.append('orderItemId', orderItemId);
        formData.append('file', fileInputs[0]);

        $.ajax({
            type: 'POST',
            url: '/review/add',
            processData: false,
            contentType: false,
            data: formData,
            success: function (response) {
                if (response.success) {
                    alert("리뷰를 작성했습니다.");
                    // 메인 모달 데이터 갱신
                    const ordersId = $('.orderId').text(); // 현재 모달에 표시된 주문 ID 가져오기
                    updateMainModalData(ordersId);
                    // 모달 닫기
                    $('#reviewModal').modal('hide');
                } else {
                    alert("리뷰 작성중 오류가 발생했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });

    //event

    // 카드 클릭 시 상세 페이지로 이동
    $(".event-card").click(function () {
        if (!$(this).hasClass('disabled-card')) {
            const url = $(this).data("url");
            window.location.href = url;
        }
    });

});
