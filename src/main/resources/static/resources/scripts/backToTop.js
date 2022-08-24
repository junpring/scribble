const btt = document.getElementById('back-to-top');
const navHeader = document.getElementById('nav');
let docElem = document.documentElement;
let offset;
let scrollPos;
// 문서 높이 계산하기
console.log('offsetHeight' + docElem.offsetHeight);
console.log('scrollHeight' + docElem.scrollHeight);
let docHeight = Math.max(docElem.offsetHeight, docElem.scrollHeight);  // 도큐먼트 높이
if(docHeight != 0){
    offset = docHeight / 4;
}

window.addEventListener('scroll', ()=> {
    scrollPos = docElem.scrollTop;
    console.log(scrollPos);
    console.log(offset);
    // navHeader.classList.add('navfixed');

    if(scrollPos < offset){
        btt.classList.add('visible');
    } else {
        btt.classList.remove('visible');
    }


});