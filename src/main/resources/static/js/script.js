$(document).ready(function () {
//     cart
    const shippingFeeThreshold = 15000; // 배송비가 무료가 되는 기준 금액
    const baseShippingFee = 3000; // 기본 배송비

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
            return $(this).closest(".cart-item").find(".cart-delete").data("cart-id");
        }).get();

        if (selectedIds.length === 0) {
            alert("삭제할 상품을 선택해주세요.");
            return;
        }

        deleteCartItems(selectedIds);
    });

    // 개별 삭제 버튼 클릭 이벤트
    $(".cart-delete").click(function () {
        const cartId = $(this).data("cart-id");
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
            const price = parseInt(cardBody.find(".price").text().replace(/[^0-9]/g, ""));
            const discountPercent = parseFloat(cardBody.find(".discount span").text().replace(/[^0-9.]/g, ""));
            const quantity = parseInt(cardBody.find(".quantity").val());

            const itemTotal = price * quantity;
            const itemDiscount = itemTotal * (discountPercent / 100);

            totalItems += quantity;
            totalPrice += itemTotal;
            totalDiscount += itemDiscount;
        });

        // 배송비 계산
        let shippingFee = totalPrice - totalDiscount >= shippingFeeThreshold ? 0 : baseShippingFee;

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
    $("#bookDetail-cartAdd").click(function () {
        const bookId = $(this).attr("data-book-id");
        const quantity = $("#quantity").val();
        $.ajax({
            url: '/cart/add',
            type: 'POST',
            traditional: true, // 배열을 쿼리 문자열로 전송하기 위해 추가
            data: {
                bookId: [bookId],
                quantity: [quantity]
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
    });
//     paymentModal
    $(".paymentModalBtn").click(function () {
        $(".modal-items").empty(); // 모달의 기존 항목 비우기

        $(".cart-checkbox:checked").each(function () {
            // 부모 요소에서 필요한 데이터를 가져옴
            const cardBody = $(this).closest(".card-body");
            const title = cardBody.find(".title").text();
            const quantity = cardBody.find(".quantity").val();
            const price = parseInt(cardBody.find(".price").text().replace(/[^0-9]/g, ""));

            const totalPrice = price * quantity;

            // 모달에 넣을 항목 생성
            const modalItem = `
            <div class="card col">
                <div class="row g-0">
                    <div class="col-md-8">
                        <div class="card-body">
                            <p>상품명: <span class="modalBookTitle">${title}</span></p>
                            <p>수량: <span class="modalQuantity">${quantity}</span></p>
                            <p>가격: <span class="modalTotalPrice">${totalPrice}</span>원</p>
                        </div>
                    </div>
                </div>
            </div>
        `;

            // 모달에 항목 추가
            $(".modal-items").append(modalItem);
        });
    });

});