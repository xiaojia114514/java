import { Login,GetUserInfo } from "@/api/login";
import {SetCookie,RemoveCookie} from "@/utils/auth";
import {
  BHome,
  AHome
} from "@/router";
import router,{resetRouter} from "@/router";
export default {
  state:{
    token:'',
    //0admin  1buyer
    permitted:0,
    permittedroutes:[],
    user:{
      UAccount: '',
      UPassword: '',
      UPhone: '',
      UAddress: '',
      UCategory: ''
    }
  },
  mutations:{
    SET_TOKEN(state,token){
      state.token=token
    },
    ADD_PERMS(state){
        if(state.permitted===0){
          state.permittedroutes=AHome()
        }else if(state.permitted===1){
          state.permittedroutes=BHome()
        }
        console.log(router)

    },
    SET_USER(state,user){
      state.user.UAccount=user.UAccount
      state.user.UAddress=user.UAddress
      state.user.UPassword=user.UPassword
      state.user.UPhone=user.UPhone
      state.user.UCategory=user.UCategory
    },
    REMOVE_TOKEN(state){
      state.token=''
    },
    REMOVE_PERMS(state){
      state.permitted=0
      resetRouter()
    }
  },
  actions:{
    Login({commit},data){
      return new Promise((resolve,reject)=>{
        Login(data).then(res=>{
          const token=res.data.data.token
          commit('SET_TOKEN',token)
          SetCookie(token)
          resolve()
        }).catch(err=>{
          reject(err)
        })
      })
    },
    GetUserInfoAction({commit},token){
      return new Promise((resolve,reject)=>{
        GetUserInfo(token).then(
          res=>{
            const user=res.data.data
            commit('SET_USER',user)
            commit('SET_TOKEN',token)
            commit('ADD_PERMS',user.privilege)
            resolve()
          }).catch(
          err=>{
            reject(err)
          }
        )
      })
    },
    UserLogout({commit}){
      RemoveCookie()
      commit('REMOVE_PERMS')
      commit('REMOVE_TOKEN')
    }
  }
}
