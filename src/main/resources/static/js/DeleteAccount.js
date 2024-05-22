Kakao.init('f9424f7a3d55eb7bb1342df28da09b3d'); // 카카오 앱 키를 설정합니다.

function login() {
    var userId = document.getElementById('userId').value;
    var password = document.getElementById('password').value;

    // Fetch API를 사용하여 로그인 요청
    fetch('https://vec-to.net/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            userId: userId,
            password: password,
            fcmToken: "temp"
        })
    }).then(response => response.json())
        .then(data => {
            console.log('Login successful:', data);
            if (data.status === 200) {
                localStorage.setItem('loginType', 'regular');
                localStorage.setItem('accessToken', data.result.accessToken);
                document.getElementById('withdrawButton').style.display = 'block';
                alert('로그인 성공!');
            } else {
                alert('로그인 실패: ' + data.message);
            }
        }).catch(error => {
        console.error('Login failed:', error);
    });
}

function loginWithKakao() {
    Kakao.Auth.login({
        success: function (authObj) {
            console.log(authObj); //access토큰 값
            Kakao.Auth.setAccessToken(authObj.access_token); //access 토큰 값 저장
            getInfo();
        },
        fail: function (err) {
            console.log(err);
        },
    });
}

function getInfo() {
    Kakao.API.request({
        url: "/v2/user/me",
        success: function (res) {
            console.log(res);
            var id = res.id;

            // Fetch API를 사용하여 로그인 요청
            fetch('https://vec-to.net/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: id,
                    fcmToken: "temp"
                })
            }).then(response => response.json())
                .then(data => {
                    console.log('Login successful:', data);
                    if (data.status === 200) {
                        localStorage.setItem('loginType', 'regular');
                        localStorage.setItem('accessToken', data.result.accessToken);
                        document.getElementById('withdrawButton').style.display = 'block';
                        alert('로그인 성공!');
                    } else {
                        alert('로그인 실패: ' + data.message);
                    }
                }).catch(error => {
                console.error('Login failed:', error);
            });

        },
        fail: function (error) {
            alert("카카오 로그인 실패" + JSON.stringify(error));
        },
    });
}

function withdraw() {
    var loginType = localStorage.getItem('loginType');
    if (loginType === 'kakao') {
        Kakao.API.request({
            url: '/v1/user/unlink',
            success: function(response) {
                console.log('Kakao unlink successful:', response);
                // 서버에 탈퇴 요청
                callWithdrawAPI();
            },
            fail: function(error) {
                console.error('Kakao unlink failed:', error);
            }
        });
    } else {
        // 일반 계정의 경우 서버에 바로 탈퇴 요청
        callWithdrawAPI();
    }
}

function callWithdrawAPI() {
    fetch('https://vec-to.net/user', {
        method: 'Delete',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
        }
    }).then(response => response.json())
        .then(data => {
            console.log('Withdraw successful:', data);
            alert('탈퇴 처리가 완료되었습니다.');
            // 필요 시 로그인 상태 해제 및 화면 리다이렉션
            localStorage.removeItem('accessToken');
            localStorage.removeItem('loginType');
            document.getElementById('withdrawButton').style.display = 'none';
        }).catch(error => {
        console.error('Withdraw failed:', error);
        alert('탈퇴 처리 중 오류가 발생하였습니다.');
    });
}
