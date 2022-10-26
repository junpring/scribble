const loginForm = window.document.getElementById('login-form');
const emailInput = loginForm['email'];
const passwordInput = loginForm['password'];

loginForm.onsubmit = (e) => {
    e.preventDefault();

    if (!emailInput.value) {
        emailInput.focus();
        return;
    }
    if (!passwordInput.value) {
        passwordInput.focus();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData(loginForm);
    xhr.open('POST', './login');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                switch (response['result']) {
                    case 'SUCCESS':
                        location.href = document.referrer;
                        break;
                    case 'DELETED':
                        alert('삭제된 이메일입니다.');
                        break;
                    case 'SUSPENDED':
                        alert('정지된 이메일입니다.');
                        break;
                    case 'EMAIL_NOT_VERIFIED':
                        alert('이메일 인증이 완료되지 않았습니다.\n이메일 인증 후 다시 로그인 해주세요.');
                        break;
                    default :
                        alert('이메일 또는 비밀번호가 일치하지 않습니다.');
                        break;
                }
            } else {
                alert('로그인 도중 오류가 발생하였습니다.\n 잠시 후 다시 시도해주세요,');
            }
        }
    };
    xhr.send(formData);
};

