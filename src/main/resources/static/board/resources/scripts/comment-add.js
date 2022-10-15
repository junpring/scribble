// const commentForm = window.document.getElementById('comment-form');
// const commentInput = commentForm['content'];
// const replyForm = window.document.getElementById('reply-form');
// const replyInput = window.document.getElementById('reply-input');
//
// commentForm.onsubmit = (e) => {
//     if (commentInput.value === "") {
//         e.preventDefault();
//         alert('내용을 입력하세요.');
//         return;
//     }
//
//     const xhr = new XMLHttpRequest();
//     const formData = new FormData(commentForm);
//     xhr.open('POST', 'comment-add');
//     xhr.onreadystatechange = () => {
//         if (xhr.readyState === XMLHttpRequest.DONE) {
//             if (xhr.status >= 200 && xhr.status < 300) {
//                 const response = JSON.parse(xhr.responseText);
//                 switch (response['comment-result']) {
//                     case 'SUCCESS':
//                         alert('댓글이 작성되었습니다.');
//                         location.reload();
//                         break;
//                     default :
//                         alert('알 수 없는 이유로 댓글 작성에 실패하였습니다.');
//                         break;
//                 }
//             } else {
//                 location.href = '/user/login';
//                 // alert('댓글 작성 도중 오류가 발생하였습니다.\n 잠시 후 다시 시도해주세요,');
//             }
//         }
//     };
//     xhr.send(formData);
// };
//
// replyForm.onsubmit = (e) => {
//     if (replyInput.value === "") {
//         e.preventDefault();
//         alert('내용을 입력하세요.');
//         return;
//     }
//     const xhr = new XMLHttpRequest();
//     const formData = new FormData(replyForm);
//     xhr.open('POST', 'reply-add');
//     xhr.onreadystatechange = () => {
//         if (xhr.readyState === XMLHttpRequest.DONE) {
//             if (xhr.status >= 200 && xhr.status < 300) {
//                 const response = JSON.parse(xhr.responseText);
//                 switch (response['reply-result']) {
//                     case 'SUCCESS':
//                         alert('댓글이 작성되었습니다.');
//                         location.reload();
//                         break;
//                     default :
//                         alert('알 수 없는 이유로 댓글 작성에 실패하였습니다.');
//                         break;
//                 }
//             } else {
//                 location.href = '/user/login';
//             }
//         }
//     };
//     xhr.send(formData);
// };