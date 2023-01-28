// +는 css와 같이 옆에 선택자를 지목함.

// const RegisterForm = {
//     getElement : () => window.document.getElementById('register-form'),
//
// }
const registerForm = window.document.getElementById('register-form');
const emailInput = registerForm['email'];
const emailWarning = registerForm.querySelector('.warning.email');
const passwordInput = registerForm['password'];
const passwordWarning = registerForm.querySelector('.warning.password');
const passwordCheckInput = registerForm['passwordCheck'];
const passwordCheckWarning = registerForm.querySelector('.warning.password-check');
const nicknameInput = registerForm['nickname'];
const nicknameWarning = registerForm.querySelector('.warning.nickname');
const contactInput = registerForm['contact'];
const loader = document.querySelector('.loader-container');


// RegExp 정규식이 담긴 상수
const emailRegex = new RegExp('^(?=.{10,100}$)([0-9a-z][0-9a-z_]*[0-9a-z])@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$');
const passwordRegex1 = new RegExp('^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,100})$');
const passwordRegex = new RegExp('^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$')
const nicknameRegex = new RegExp('^([0-9a-zA-Z가-힣]{2,10})$');
const contactRegex = new RegExp('^([0-9]{11})$');

// 우편번호
const addressSearchButton = registerForm.querySelector('.address-search-button');
const addressSearch = window.document.getElementById('address-search');
const addressSearchContainer = window.document.querySelector('.container');
const addressPostalInput = registerForm['addressPostal'];
const addressPostalRegex = new RegExp('^([0-9]{5})$');
const addressPrimaryInput = registerForm['addressPrimary'];
const addressPrimaryRegex = new RegExp('^(?=.{8,100})([가-힣][0-9가-힣 ]*[0-9])$');
const addressSecondaryInput = registerForm['addressSecondary'];
const checkAll = document.getElementById('check_all');
const registerCheckboxes = document.querySelectorAll('input[type="checkbox"]');

const showAddressPopUp = () => {
    addressSearch.classList.add('visible');
}
const hideAddressPopUp = () => {
    addressSearch.classList.remove('visible');
}

let emailChecked = null;
let nicknameChecked = null;
let passwordChecked = null;


// xhr
passwordInput.addEventListener('keyup', () => {
    if (passwordInput.value === '') {
        passwordWarning.innerText = '';
        return;
    }
    if (!passwordRegex.test(passwordInput.value)) {
        passwordWarning.innerText = '비밀번호 형식이 올바르지 않습니다. \n숫자, 문자, 특수문자가 조합된 8~20자';
        passwordWarning.style.color = '#000000';
        passwordWarning.style.display = 'inline';
    } else {
        passwordWarning.innerText = '사용 가능한 비밀번호 입니다.';
        passwordWarning.style.display = 'inline';
        passwordWarning.style.color = '#6167b2';
    }
});
passwordCheckInput.addEventListener('keyup', () => {
    if (passwordCheckInput.value === '') {
        passwordCheckWarning.innerText = '';
        return;
    }
    if (passwordInput.value !== passwordCheckInput.value) {
        passwordCheckWarning.innerText = '비밀번호가 일치하지 않습니다.';
        passwordCheckWarning.style.display = 'inline';
        passwordCheckWarning.style.color = '#f86161';
        passwordChecked = false;
    }
    if (passwordInput.value === passwordCheckInput.value && passwordCheckInput.value !== '') {
        passwordCheckWarning.innerText = '비밀번호가 일치합니다.';
        passwordCheckWarning.style.display = 'inline';
        passwordCheckWarning.style.color = '#6167b2';
        passwordChecked = true;
    }
});


emailInput.addEventListener('keyup', () => {
    if (emailInput.value === '') {
        emailWarning.innerText = '';
        return;
    }
    if (!emailRegex.test(emailInput.value)) {
        emailWarning.innerText = '이메일 형식이 올바르지 않습니다.'
        emailWarning.style.display = 'inline';
        emailWarning.style.color = '#f86161';
    } else {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('email', emailInput.value);
        xhr.open('POST', 'check-email');
        xhr.onreadystatechange = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    const response = parseInt(xhr.responseText);
                    switch (response) {
                        case 0:
                            emailWarning.innerText = '사용 가능한 이메일입니다.';
                            emailWarning.style.display = 'inline';
                            emailWarning.style.color = '#6167b2';
                            emailChecked = true;
                            break;
                        case 1:
                            emailWarning.innerText = '해당 이메일은 이미 사용 중입니다.';
                            emailWarning.style.display = 'inline';
                            emailWarning.style.color = '#000000';
                            emailChecked = false;
                            break;
                        default:
                            emailWarning.innerText = '이메일 형식이 올바르지 않습니다.';
                            emailWarning.style.display = 'inline';
                            emailChecked = false;
                            break;
                    }
                }
            }
        };
        xhr.send(formData);
    }
});
// readyState : 준비상태 ,  DONE : 요청 완료
nicknameInput.addEventListener('keyup', () => {
    if (nicknameInput.value === '') {
        nicknameWarning.innerText = '';
        return;
    }
    if (!nicknameRegex.test(nicknameInput.value)) {
        nicknameWarning.innerText = '닉네임 형식이 올바르지 않습니다.'
        nicknameWarning.style.display = 'inline';
        nicknameWarning.style.color = '#f86161';
    } else {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('nickname', nicknameInput.value);
        xhr.open('POST', 'check-nickname');
        xhr.onreadystatechange = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    const response = parseInt(xhr.responseText);
                    switch (response) {
                        case 0:
                            nicknameWarning.innerText = '사용 가능한 닉네임입니다.';
                            nicknameWarning.style.display = 'inline';
                            nicknameWarning.style.color = '#6167b2';
                            nicknameChecked = true;
                            break;
                        case 1:
                            nicknameWarning.innerText = '해당 닉네임은 이미 사용 중입니다.';
                            nicknameWarning.style.display = 'inline';
                            nicknameWarning.style.color = '#000000'
                            nicknameChecked = false;
                            break;
                        default:
                            nicknameWarning.innerText = '올바른 닉네임을 입력해주세요.';
                            nicknameWarning.style.display = 'inline';
                            nicknameChecked = false;
                            break;
                    }
                }
            }

        }
        xhr.send(formData);
    }
});


// daum API
addressSearchButton.addEventListener('click', () => {
    showAddressPopUp();
    new daum.Postcode({
        oncomplete: e => {
            registerForm['addressPostal'].value = e.zonecode;
            registerForm['addressPrimary'].value = e.address;
            registerForm['addressSecondary'].value = '';
            registerForm['addressSecondary'].focus();
            hideAddressPopUp();
        },
        theme: {
            searchBgColor: "#6167b2", //검색창 배경색
            queryTextColor: "#FFFFFF", //검색창 글자색
            emphTextColor: "#6167b2", //강조 글자색
        }
    }).embed(addressSearchContainer);
});
document.addEventListener('mouseup', () => {
    hideAddressPopUp();
})


function selectAll(selectAll) {
    registerCheckboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    });
}

checkAll.addEventListener('click', () => {
    selectAll(checkAll);
});


// 회원가입 버튼 실행

registerForm.onsubmit = (e) => {
    e.preventDefault();
    if (!confirm('회원가입을 진행하시겠습니까?')) {
        e.preventDefault();
        return;
    }
    for (let i = 0; i < registerCheckboxes.length; i++) {
        if (i !== 0 && !registerCheckboxes[i].checked) {
            alert('이용약관을 읽고 동의해주세요.');
            e.preventDefault();
            return;
        }
    }

    // 이메일
    if (!emailRegex.test(emailInput.value)) {
        alert('이메일 형식이 올바르지 않습니다.');
        emailInput.focus();
        emailInput.select();
        return;
    }
    // 패스워드 체크
    if (!passwordRegex.test(passwordInput.value)) {
        alert('비밀번호 형식이 올바르지 않습니다.\n 숫자, 문자, 특수문자가 조합된 8~20자로 다시 작성해주세요.');
        passwordInput.focus();
        passwordInput.select();
        return;
    }
    if (passwordChecked !== true) {
        alert('비밀번호가 일치하지 않습니다.');
        passwordCheckInput.focus();
        passwordCheckInput.select();
        return;
    }
    // 닉네임 체크
    if (!nicknameRegex.test(nicknameInput.value)) {
        alert('닉네임 형식이 올바르지 않습니다.');
        nicknameInput.focus();
        nicknameInput.select();
        return;
    }
    if (!contactRegex.test(contactInput.value)) {
        alert('휴대폰 번호가 올바르지 않습니다.');
        contactInput.focus();
        contactInput.select();
        return;
    }
    if (!addressPostalRegex.test(addressPostalInput.value)) {
        alert('우편번호가 올바르지 않습니다.');
        addressPostalInput.focus();
        return;
    }
    if (!addressPrimaryRegex.test(addressPrimaryInput.value)) {
        alert('주소가 올바르지 않습니다.');
        addressPrimaryInput.focus();
        return;
    }
    if (!addressSecondaryInput.value) {
        alert('상세주소가 올바르지 않습니다.');
        addressSecondaryInput.focus();
        addressSecondaryInput.select();
        return;
    }
    loader.classList.add('visible');

    const xhr = new XMLHttpRequest();
    const formData = new FormData(registerForm);
    xhr.open('POST', 'register');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                switch (response['result']) {
                    case 'DUPLICATE_EMAIL':
                        e.preventDefault();
                        emailInput.focus();
                        emailInput.select();
                        loader.classList.remove('visible');
                        alert('해당 이메일은 이미 사용 중입니다.');
                        break;
                    case 'DUPLICATE_NICKNAME':
                        e.preventDefault();
                        nicknameInput.focus();
                        nicknameInput.select();
                        loader.classList.remove('visible');
                        alert('해당 닉네임은 이미 사용 중입니다.');
                        break;
                    case 'DUPLICATE_CONTACT':
                        e.preventDefault();
                        contactInput.focus();
                        contactInput.select();
                        loader.classList.remove('visible');
                        alert('해당 번호은 중복된 번호입니다.');
                        break;
                    case 'SUCCESS':
                        alert('임시 회원가입이 완료되었습니다.\n이메일 인증 후 로그인 해주세요.');
                        location.href = '/';
                        break;
                    default :
                        alert('알 수 없는 이유로 회원가입에 실패하였습니다.');
                        break;
                }
            } else {
                alert('회원가입 도중 오류가 발생하였습니다.\n 잠시 후 다시 시도해주세요,');
            }
        }
    };
    xhr.send(formData);

}



