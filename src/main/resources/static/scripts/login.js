Vue.component('login', {
  template: '#login-template',
  data(){
    return {
      username: '',
      password: ''
    }
  },
  method: {
    login () {
      fetch('/login', {
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
        console.log('res: ', res);
      })
    }
  }
})

new Vue({
  el: '#main'
})