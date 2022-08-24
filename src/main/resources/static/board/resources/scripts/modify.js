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
    });
config.disableNativeSpellChecker = false;





const mainForm = window.document.getElementById('main');
const titleInput = mainForm['title'];
const categorySelect = mainForm['boardId'];
const cancelButton = window.document.getElementById('cancel-button');

cancelButton.onclick = () => {
    if (confirm('정말로 취소할까요? 수정하신 정보가 모두 유실됩니다.')) {
        window.history.back();
    }
}

mainForm.onsubmit = (e) => {
    if (!titleInput.value) {
        alert("글 제목을 입력해주세요.");
        titleInput.focus();
        e.preventDefault();
    }

};
