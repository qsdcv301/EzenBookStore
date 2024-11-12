$(document).ready(function () {
//     cart
    $(".cart-delete").click(function () {
        const cartId = $(this).attr("data-cart-id");
        $.ajax({
            url: '/cart/delete',
            type: 'POST',
            traditional: true, // 배열을 쿼리 문자열로 전송하기 위해 추가
            data: {
                cartId: [cartId]
            },
            success: function (response) {
                if (response.success) {
                    alert("해당 상품이 삭제 되었습니다.");
                    location.reload();
                } else {
                    alert("해상 상품을 삭제 실패했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });
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
});