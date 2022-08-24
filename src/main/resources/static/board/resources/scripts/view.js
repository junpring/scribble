const moreButton = document.querySelector('.more');
const urlLink = document.querySelector('.url-link');
const morePopper = document.querySelector('.more-popper');

// 현재 url 복사 클립보드에 복사
CopyUrlToClipboard = () => {
    var dummy = document.createElement("input");
    var text = location.href;
    document.body.appendChild(dummy);
    dummy.value = text;
    dummy.select();
    document.execCommand("copy");
    document.body.removeChild(dummy);
}

// documentMouseUp = () => {
//     document.addEventListener('mouseup', () => {
//         morePopper.style.display = null;
//     });
// }

moreButton.addEventListener('click', () => {
    if (morePopper.style.display === '') {
        morePopper.style.display = 'block';
    } else {
        morePopper.style.display = null;
    }

});


urlLink.onclick = () => {
    CopyUrlToClipboard();
    alert('클립보드에 복사되었습니다.');
}














