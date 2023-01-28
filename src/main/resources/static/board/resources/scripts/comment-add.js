cancelButton.onclick = () => {
    if (confirm('정말로 취소할까요? 작성하신 정보가 모두 유실됩니다.')) {
        window.history.back();
    }
}