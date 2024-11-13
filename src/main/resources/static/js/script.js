$(document).ready(function () {
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
            totalDiscount += itemDiscount;
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
            const itemDiscount = itemTotalPrice * (discount / 100);
            const itemDiscountedPrice = itemTotalPrice - itemDiscount;

            // 총합 계산
            totalItems += quantity;
            totalOriginalPrice += itemTotalPrice;
            totalDiscountedPrice += itemDiscountedPrice;
            totalDiscount += itemDiscount;

            // 모달에 표시할 개별 상품 정보 HTML 생성
            const modalItem = `
            <div class="card col">
                <div class="row g-0">
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
            const updatedUsedPoints = parseInt($(this).val()) || 0;
            updateOrderSummary(totalOriginalPrice, totalDiscount, shippingFee, totalDiscountedPrice, updatedUsedPoints);
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
});