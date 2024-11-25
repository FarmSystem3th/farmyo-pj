// reCAPTCHA 콜백 설정
var onloadCallback = function () {
    console.log("reCAPTCHA 렌더링 시작");
    grecaptcha.render("g-recaptcha", {
        sitekey: "6LfnV4kqAAAAAEjqT4M0Swr7tmixyXho1t3mDAZF", // 실제 Site Key를 사용하세요
        callback: verifyCallback,
        "expired-callback": expiredCallback,
    });
};

// 인증 성공 시
var verifyCallback = function (response) {
    console.log("reCAPTCHA 인증 성공, response:", response);
    $("#loginBtn").removeClass("disabled-btn");
    $("#loginBtn").attr("disabled", false);
};

// 인증 만료 시
var expiredCallback = function () {
    console.log("reCAPTCHA 인증 만료");
    $("#loginBtn").addClass("disabled-btn");
    $("#loginBtn").attr("disabled", true);
};

// g-recaptcha 리셋
var resetCallback = function () {
    console.log("reCAPTCHA 리셋");
    grecaptcha.reset();
};

// 로그인 버튼 클릭 이벤트
$("#loginBtn").on("click", function () {
    if ($("#loginBtn").hasClass("disabled-btn")) {
        alert("reCAPTCHA 인증 후 진행이 가능합니다.");
        return;
    }

    var email = $("#loginId").val(); // 이메일 필드로 수정
    var password = $("#loginPw").val(); // 비밀번호 필드로 수정
    var recaptcha = $("#g-recaptcha-response").val();

    // 입력값 검증
    if (!email || !password) {
        alert("아이디와 비밀번호를 입력해주세요.");
        return;
    }

    if (!recaptcha) {
        alert("reCAPTCHA 인증에 실패했습니다. 다시 시도해주세요.");
        return;
    }

    // AJAX 요청
    console.log("AJAX 요청 데이터:", {
        email: email, // 수정된 필드
        password: password, // 수정된 필드
        recaptchaToken: recaptcha, // 수정된 필드
    });

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "http://localhost:8080/login", // 서버의 로그인 API 엔드포인트
        data: JSON.stringify({
            email: email, // 수정된 필드
            password: password, // 수정된 필드
            recaptchaToken: recaptcha, // 수정된 필드
        }),
        dataType: "JSON",
        success: function (data) {
            console.log("AJAX 응답 데이터:", data); // 응답 데이터 확인
            if (data.status === true) {
                alert("로그인 성공!");
                window.location.href = "/dashboard"; // 성공 후 리다이렉트
            } else {
                alert(data.errMsg);
            }
        },
        error: function (err) {
            console.error("AJAX 요청 실패:", err);
            alert("서버 에러 발생: " + err.responseText);
        },
    });
});
