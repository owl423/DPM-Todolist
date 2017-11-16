Vue.component('register', {
  template: '#register-template',
  data(){
    return {
      username: '',
      password: '',
      usernameValidation: true,
      passwordValidation: true,
      confirmPasswordValidation: true,
      setTimeoutId: null
    }
  },
  methods: {
    register(){
      let _this = this;
      if(this.checkAllValidationMsg()) {
        fetch('/register', {
          method: 'post',
          headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            username: this.username,
            password: this.password
          })
        }).then(res => {
          if(res.status === 201) {
            alret('회원가입이 완료되었습니다.');
          }
        }).catch(res => {
          if(res.status === 409) {
            alret('아이디가 중복 됩니다.');
            _this.usernameValidation = false;
          } else {
            alert('회원가입에 실패했습니다.')
          }
        })
      }
    },
    validateUsername(username){
      if(username.length > 45 
        || username.length < 4 
        || /[^A-Za-z0-9_]/.test(username)) {
        this.usernameValidation = false;
        return false;
      }
      this.usernameValidation = true;
      return true;
    },
    validatePassword(password){
      if(password.length < 10 || password.length > 50){
        this.passwordValidation = false;
        return false;
      }
      this.passwordValidation = true;
      return true;
    },
    validateConfirmPassword(confirmPassword){
      if(this.password !== confirmPassword){
        this.confirmPasswordValidation = false;
        return false;
      }
      this.confirmPasswordValidation = true;
      return true;
    },
    validate(target, input, validateFun){
      if(this.setTimeoutId){
        clearTimeout(this.setTimeoutId);
      } 
      this.setTimeoutId = setTimeout(() => {
        if(validateFun(input)){
          this[target] = input;
        }
      }, 300);
    },
    checkUsername(e){
      let input = e.target.value;
      this.validate('username', input, this.validateUsername);
    },
    checkPassword(e){
      let input = e.target.value;
      this.validate('password', input, this.validatePassword);
    },
    confirmPassword(e){
      let input = e.target.value;
      this.validate('password', input, this.validateConfirmPassword);
    },
    checkAllValidationMsg(){
      if(!this.username || !this.usernameValidation) {
        this.$refs.inputUsername.focus();
        return false;
      }
      if(!this.password || !this.passwordValidation) {
        this.$refs.inputPassword.focus();
        return false;
      }
      if(!this.confirmPasswordValidation) {
        this.$refs.inputConfirmPassword.focus();
        return false;
      }
      return true;
    }
  }
})

new Vue({
  el: '#main'
});