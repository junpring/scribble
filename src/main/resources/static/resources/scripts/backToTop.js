const btt = document.getElementById('back-to-top');
let docElem = window.document.documentElement;
let offset;
let scrollPos;
// 문서 높이 계산하기
console.log('--------------------');
console.log('docElem' + docElem);
console.log('offsetHeight' + docElem.offsetHeight);
console.log('scrollHeight' + docElem.scrollHeight);
let docHeight = Math.max(docElem.offsetHeight, docElem.scrollHeight);  // 도큐먼트 높이
if(docHeight !== 0){
    offset = docHeight;
}

window.addEventListener('scroll', ()=> {
    scrollPos = document.body.scrollTop;
    console.log(scrollPos);
    if(scrollPos > offset){
        btt.classList.add('visible');
    } else {
        btt.classList.remove('visible');
    }


});