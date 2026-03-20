const get={
  token: state=>state.user.token,
  permmited: state=>state.user.permitted,
  UAccount: state=>state.user.user.UAccount,
  permittedRoutes: state=>state.user.permittedroutes,
  user:state=>state.user.user
}
export default get
