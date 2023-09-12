<template>
</template>
<script>
import { axios } from '@/utils/request'
export default {
  name: 'WebSocketComponents',
  data () {
    return {
      socket: ''
   }
  },
  methods: {
    initWebSocket() {
        this.socket.onerror = this.setErrorMessage
        this.socket.onopen = this.setOnopenMessage
        console.log("连接建立成功：" + this.wsUrl)
        this.socket.onmessage = this.setOnMessage
        this.socket.onclose = this.setOncloseMessage
        window.onbeforeunload = this.onbeforeunload
    },
    setOnMessage(event) {
        const noticeMsg = JSON.parse(event.data);
        // this.msgs[Number(noticeMsg.msgNo)-1].msg = noticeMsg.msg
        // this.showNotice[Number(noticeMsg.msgNo)-1].show = !(noticeMsg.msg === undefined)
        this.$notification.info( { message: noticeMsg.noticeMsg , description: noticeMsg.noticeDescription})
        this.$emit('noticeInit');
    },
    setErrorMessage () {
      console.log('WebSocket连接发生错误   状态码：' + this.socket.readyState)
    },
    setOnopenMessage () {
      console.log('WebSocket连接成功    状态码：' + this.socket.readyState)
    },
    setOnmessageMessage (event) {
      console.log('服务端返回：' + event.data)
    },
    setOncloseMessage () {
      console.log('WebSocket连接关闭    状态码：' + this.socket.readyState)
    },
    onbeforeunload () {
      this.closeWebSocket()
    },
    closeWebSocket () {
      this.socket.close()
    },
 },
//如果嵌入了该组件，向后台请求websocket地址 不为空即建立websocket连接
 created() {
    const that = this;
    axios.get(`/socket/getWsAddress`).then( (response) => {
      if ("" == response) {
        return;
      }
      // Return a string that indicates how binary data from the WebSocket object is exposed to scripts
      that.socket = new WebSocket(response);
      that.initWebSocket();
　　　　//这里可以在websocket连接成功后，去调用父组件的初始化的方法
      that.$emit('noticeInit');
    });
  }
}
</script>
