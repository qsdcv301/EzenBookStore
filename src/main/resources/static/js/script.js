$(document).ready(function () {
//     cart

//     bookDetail
    $("#bookDetail-cartAdd").click(function () {
        const bookId = $("#bookId").val();
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
                    alert("장바구니에 추가되었습니다.");
                } else {
                    alert("장바구니 추가 실패했습니다.");
                }
            },
            error: function () {
                alert("서버 오류가 발생했습니다.");
            }
        });
    });
});