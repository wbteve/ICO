/**
 *
 * 环境配置，及登陆逻辑
 *
 **/
(function(owner) {
    /**
     * 默认配置信息
     */
    var serverUrl = 'http://192.168.0.191';


    // VARIABLES =============================================================
    var TOKEN_KEY = "$adminToken";
    var USER_KEY = "$adminUser";

    /**
     * 常量定义
     */
    owner.ADMIN = "ROLE_ADMIN";	// 超级管理员
    owner.USER = "ROLE_USER";	// 管理员



    owner.createAuthorizationTokenHeader = function () {
        var token = owner.getToken();
        if (token) {
            return {"Authorization": token};
        } else {
            return {};
        }
    }

    /**
     * 设置用户信息
     **/
    owner.setUserInfo = function(userInfo) {
        userInfo = userInfo || {};
        localStorage.setItem(USER_KEY, JSON.stringify(userInfo));
    }

    /**
     * 获取用户信息， 不存在返回空对象
     * @return {Object} 登陆用户信息
     **/
    owner.getUserInfo = function() {
        var userInfo = localStorage.getItem(USER_KEY) || '{}';
        return JSON.parse(userInfo);
    }

    /**
     * 判断用户状态是否有效
     *
     * @return {boolean}
     */
    owner.isUserValid = function() {
        var jwtToken = app.getToken();

        if(Object.keys(jwtToken).length) {
            var decodedToken = jwt_decode(jwtToken);

            var expireDate = new Date(decodedToken.exp * 1000);

            if(expireDate >= new Date()) {
                return true;
            } else {
                // 清空旧用户信息
                app.setUserInfo({});
                return false;
            }
        } else {
            // 清空旧用户信息
            app.setUserInfo({});
            return false;
        }
    };

    /**
     * 清空用户状态
     */
    owner.clearState = function () {
        app.setUserInfo({});
        app.setToken({});
    }

    /**
     * 获取当前Token
     **/
    owner.getToken = function() {
        var state = localStorage.getItem(TOKEN_KEY) || '{}';
        return JSON.parse(state);
    };

    /**
     * 设置当前Token
     **/
    owner.setToken = function(state) {
        state = state || {};
        localStorage.setItem(TOKEN_KEY, JSON.stringify(state));
    };

    /**
     * 获取当前服务器地址
     **/
    owner.getServerUrl = function() {
        return serverUrl;
    };

    owner.getQueryString =  function(name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)
            return  unescape(r[2]);
        return null;
    }

    owner.sendSMS = function(phone){
        var phoneJson = {"phone": phone};
        // 此处调用发送手机验证码的接口获取手机验证码
        $.ajax({
            url: "/user/validatePhone",
            type: 'POST',
            timeout: 10000,//超时时间设置为10秒；
            data: JSON.stringify(phoneJson),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: app.createAuthorizationTokenHeader(),
            success: function (result) {
                if (result.code == 200) {
                    app.setUserInfo(result.data.userInfo);
                }
                else {
                    sweetAlert(result.message);
                }
            },
            error: function (xhr, type, errerThrown) {
                sweetAlert("网络异常,请稍候再试!");
            }
        });
    }

    owner.myAlertWithHtml = function(title, text, type, jumphtml){
        sweetAlert({
            title: title,
            text: text,
            type: type,
            confirmButtonColor: "#d9534f"
        },function () {
            if(jumphtml){
                location.href=jumphtml;
            }
        });
    };
    owner.myAlert = function(title, text, type){
        sweetAlert({
            title: title,
            text: text,
            type: type,
            confirmButtonColor: "#d9534f"
        });
    };

}(window.app = {}));
