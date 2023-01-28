const searchButton = document.querySelector('.search-button');
const keywordInput = document.querySelector('.keyword-input');


function formatDate(date) {
    let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    hour = d.getHours();
    minute = d.getMinutes();
    second = d.getSeconds();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day, hour, minute, second].join('-');
}

function formatDateAdd(date) {
    let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    hour = d.getHours();
    minute = '' + d.getMinutes();
    second = d.getSeconds();


    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    if (minute.length === 1) {
        minute = '0' + minute;
        console.log(minute.length);
    }

    return [year, month, day].join('-') + ' ' + [hour, minute].join(':');
}

function maskingName(strName) {
    if (strName.length > 2) {
        let originName = strName.split('');
        originName.forEach(function (name, i) {
            if (i === 0 || i === originName.length - 1) return;
            originName[i] = '*';
        });
        let joinName = originName.join();
        return joinName.replace(/,/g, '');
    } else {
        let pattern = /.$/; // 정규식
        return strName.replace(pattern, '*');
    }
};

function timeForToday(value) {
    const today = new Date();
    const timeValue = new Date(value);

    const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
    if (betweenTime < 1) return '방금전';
    if (betweenTime < 60) {
        return `${betweenTime}분전`;
    }

    const betweenTimeHour = Math.floor(betweenTime / 60);
    if (betweenTimeHour < 24) {
        return `${betweenTimeHour}시간전`;
    }

    const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
    if (betweenTimeDay < 365) {
        return `${betweenTimeDay}일전`;
    }

    return `${Math.floor(betweenTimeDay / 365)}년전`;
}

function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}


searchButton.addEventListener('click', function (e) {
    if (keywordInput.value === '') {
        e.preventDefault();
        alert('검색어를 입력해 주세요.');
    }
});

const favorite = window.document.getElementById('favorite');

// cross browsing
function tete() {

}
    favorite.addEventListener('click', function(e) {
        let bookmarkURL = window.location.href;
        let bookmarkTitle = document.title;
        let triggerDefault = false;

        if (window.sidebar && window.sidebar.addPanel) {
            // Firefox version < 23
            window.sidebar.addPanel(bookmarkTitle, bookmarkURL, '');
        } else if ((window.sidebar && (navigator.userAgent.toLowerCase().indexOf('firefox') > -1)) || (window.opera && window.print)) {
            // Firefox version >= 23 and Opera Hotlist
            let $this = this;
            $this.attr('href', bookmarkURL);
            $this.attr('title', bookmarkTitle);
            $this.attr('rel', 'sidebar');
            $this.off(e);
            triggerDefault = true;
        } else if (window.external && ('AddFavorite' in window.external)) {
            // IE Favorite
            window.external.AddFavorite(bookmarkURL, bookmarkTitle);
        } else {
            // WebKit - Safari/Chrome
            alert((navigator.userAgent.toLowerCase().indexOf('mac') != -1 ? 'Cmd' : 'Ctrl') + '+D 키를 눌러 즐겨찾기에 등록하실 수 있습니다.');
        }

        return triggerDefault;
    });








// document.addEventListener("DOMContentLoaded", function () { // html에서 script코드 짜서할 때
//
// })

