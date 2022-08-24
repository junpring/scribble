document.getElementById('delete').onclick = () => {
    if (!confirm('정말로 게시글을 삭제할까요?')) {
        // e.preventDefault();
    } else {
        alert("게시글이 삭제되었습니다.");
    }
};