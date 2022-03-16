// 주소api
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

// 주소 체크
function address() {
    $("#memberAddress").attr("value",$("#sample6_postcode").val() + "_" + $("#sample6_address").val() + " " + $("#sample6_detailAddress").val() + " " +$("#sample6_extraAddress").val());

    const address = $("#sample6_address").val();
    const detailAddress = $("#sample6_detailAddress").val();
    const checkResult=document.getElementById("addressOut");

    if(address.length==0 || detailAddress.length==0 ){
        checkResult.style.color = 'red';
        checkResult.innerText='필수입력 항목입니다. 주소와 상세주소를 기입해주세요';
    }
    else {
        checkResult.style.color = 'green';
        checkResult.innerText='GOOD';
    }

}

// 닉네임 체크
function nicknameDuplicateCheck(){
    const exp = /^(?=.*[a-zA-Z\d_])[a-zA-Z\d_]{2,18}$/;
    const nickname=$("#memberNickname").val();
    const checkResult=document.getElementById("nicknameDuplicate");
    $.ajax({
        type:'post',
        url:"nicknameDuplicate",
        data:{"nickname" : nickname},
        success : function (result){
            if(result=="ok"){
                if(nickname.length==0){
                    checkResult.innerHTML="필수 입력값입니다.";
                    checkResult.style.color="red";
                }
                else if(nickname.match(exp)){
                    checkResult.innerHTML="GOOD";
                    checkResult.style.color="green"
                }
                else{
                    checkResult.innerHTML="유효하지 않은 형식입니다. 영문 대 소문자, 숫자, 특수문자('_')중에서 입력하세요";
                    checkResult.style.color="red"
                }
            }
            else{
                checkResult.style.color = 'red';
                checkResult.innerHTML='이미 사용중인 아이디입니다.';
            }
        },
        error : function (){
            console.log("닉네임체크 오류")
        }
    });
}

// 핸드폰번호 체크
function pnCheck(){
    const exp = /^(?=.*\d)[\d]{11}$/;
    const phone = document.getElementById('memberPhone').value;
    const checkResult = document.getElementById('pnOut');

    if(phone.match(exp)){
        checkResult.innerHTML ="GOOD"
        checkResult.style.color="green";
    }
    else if(phone.length==0){
        checkResult.innerHTML="필수 입력값입니다.";
        checkResult.style.color="red";
    }
    else{
        checkResult.innerHTML="유효하지 않은 형식입니다. 11자리의 숫자를 입력하세요."
        checkResult.style.color="red"
    }

}

function joinSubmit(){

    nicknameDuplicateCheck();
    pnCheck();
    address();

    const name=document.getElementById("nicknameDuplicate").innerText
    const addresst=document.getElementById("addressOut").innerText
    const phone=document.getElementById("pnOut").innerText

    if(name=='GOOD' && addresst=='GOOD' && phone=="GOOD")
        joinForm.submit();
    else {

    }
}
