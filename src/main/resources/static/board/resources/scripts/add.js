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
const categorySelect = mainForm['boardId'];
const cancelButton = window.document.getElementById('cancel-button');

cancelButton.onclick = () => {
    if (confirm('정말로 취소할까요? 작성하신 정보가 모두 유실됩니다.')) {
        window.history.back();
    }
}

mainForm.onsubmit = (e) => {
    if (!titleInput.value) {
        e.preventDefault();
        alert("글 제목을 입력해주세요.");
        titleInput.focus();
        return;
    }
    if (categorySelect.options[categorySelect.selectedIndex].value === 'none') {
        e.preventDefault();
        alert("게시판 종류를 선택해주세요.");
        categorySelect.focus();
        return;
    }
    if (confirm("정말로 게시글을 작성하시겠습니까?")){
        alert('작성이 완료되었습니다.');
    }else {
        e.preventDefault();
    }
};




// case 'SUCCESS':
// alert('게시글 작성이 완료되었습니다.');
// location.href = '/board/' + categorySelect.options[categorySelect.selectedIndex].value;
// break;