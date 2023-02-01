ClassicEditor
    .create(window.document.getElementById('editor'), {
        simpleUpload: {
            uploadUrl: '/board/add/upload-image'
        }
    })

    .then(editor => {
        editor.editing.view.change(writer => {
            writer.setStyle('min-height', '20rem', editor.editing.view.document.getRoot());
            writer.setStyle('min-height', '70vh', editor.editing.view.document.getRoot());
        });
    })


const mainForm = window.document.getElementById('main');
const titleInput = mainForm['title'];
const categorySelect = mainForm['boardCode'];
const cancelButton = window.document.getElementById('cancel-button');
const contentInput = mainForm['content'];
const loader = document.querySelector('.loader-container');

cancelButton.onclick = () => {
    if (confirm('정말로 취소할까요? 작성하신 정보가 모두 유실됩니다.')) {
        window.history.back();
    }
}
mainForm.onsubmit = (e) => {
    if (categorySelect.options[categorySelect.selectedIndex].value === 'none') {
        e.preventDefault();
        alert('게시판 종류를 선택해주세요.');
        categorySelect.focus();
        return;
    }

    if (!titleInput.value) {
        e.preventDefault();
        alert('글 제목을 입력해주세요.');
        titleInput.focus();
        return;
    }

    if (!contentInput.value) {
        e.preventDefault();
        alert('글 내용을 입력해주세요.');
        contentInput.focus();
        return;
    }

    if (confirm('정말로 게시글을 작성하시겠습니까?')) {
        loader.classList.add('visible');
        alert('게시글이 생성되었습니다.');
    }else {
        return false;
    }
};

// Back Forward Cache로 브라우저가 로딩될 경우 혹은 브라우저 뒤로가기 했을 경우
window.onpageshow = function(event) {
    if ( event.persisted || (window.performance && window.performance.navigation.type === 2)) {
        history.back();
    }
}

/*
 case 'SUCCESS':
 alert('게시글 작성이 완료되었습니다.');
 location.href = '/board/' + categorySelect.options[categorySelect.selectedIndex].value;
 break;
 */