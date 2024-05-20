async function submitLoginForm(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    try {
        const response = await fetch('/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.text();
            alert(result); //
            window.location.href = "/";
        } else {
            alert("로그인에 실패했습니다.");
        }
    } catch (error) {
        alert("서버와 통신 중 오류가 발생했습니다.");
    }
}