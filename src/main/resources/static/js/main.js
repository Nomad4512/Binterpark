// 메인페이지 -> 회원가입 페이지
function moveToSignupForm() {
    window.location.href = '/join';
}

// 메인페이지 -> 로그인 페이지
function moveToLoginForm() {
    window.location.href = '/login';
}

// 회원가입 제출
async function submitSignupForm(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    try {
        const response = await fetch('/api/users/join', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.text();
            alert(result); // "회원가입이 완료되었습니다" 메시지
            window.location.href = "/";
        } else {
            alert("회원가입에 실패했습니다.");
        }
    } catch (error) {
        alert("서버와 통신 중 오류가 발생했습니다.");
    }
}