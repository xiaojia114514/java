// store.js
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    UAccount: ''
  },
  mutations: {
    setUAccount(state, UAccount) {
      state.UAccount = UAccount;
    }
  },
  actions: {
    login({ commit }, UAccount) {
      commit('setUAccount', UAccount);
    }
  }
});
