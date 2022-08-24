const commentForm = window.document.getElementById('comment-form');
const commentInput = commentForm['content'];

commentForm.onsubmit = (e) => {
    if (commentInput.value === "") {
        e.preventDefault();
        alert('내용을 입력하세요.');
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData(commentForm);
    xhr.open('POST', 'comment-add');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                switch (response['comment-result']) {
                    case 'USER_NOT_FOUND':
                        alert('로그인 후 댓글 작성이 가능합니다.');
                        location.href = '/user/login';
                        break;
                    case 'SUCCESS':
                        location.reload();
                        break;
                    default :
                        alert('알 수 없는 이유로 댓글 작성에 실패하였습니다.');
                        break;
                }
            } else {
                alert('댓글 작성 도중 오류가 발생하였습니다.\n 잠시 후 다시 시도해주세요,');
            }
        }
    };
    xhr.send(formData);
};