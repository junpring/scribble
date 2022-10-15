const moreButton = document.querySelector('.more');
const urlLink = document.querySelector('.url-link');
const urlLinkCopy = document.querySelector('.url-link > li > .url-link-copy');
const morePopper = document.querySelector('.more-popper');



// 현재 url 복사 클립보드에 복사
CopyUrlToClipboard = () => {
    let dummy = document.createElement("input");
    let text = location.href;
    document.body.appendChild(dummy);
    dummy.value = text;
    dummy.select();
    document.execCommand("copy");
    document.body.removeChild(dummy);
}
urlLinkCopy.onclick = () => {
    CopyUrlToClipboard();
    alert('클립보드에 복사되었습니다.');
}

moreButton.addEventListener('click', () => {
    if (morePopper.style.display === '') {
        morePopper.style.display = 'block';
    } else {
        morePopper.style.display = '';
    }
});
document.addEventListener('mouseup', () => {
    morePopper.style.display = '';
})














