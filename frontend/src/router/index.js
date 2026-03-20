import { createRouter, createWebHistory } from 'vue-router'
import AHome from "@/view/admin/Home-admin.vue";
import BHome from "@/view/buyer/Home-buyer.vue";
import Login from "@/view/login-home.vue";
import Ishell from "@/components/block-admin/shelf-ing.vue";
import UserIn from "@/view/buyer/Home-buyerInfor.vue";
import UpdatePwd from "@/components/block-admin/admin-pwd.vue";
import UpdateGood from "@/view/admin/admin-Update.vue";
import AProductDet from "@/components/block-admin/admin-detail.vue";
import BProductDet from "@/view/buyer/buyer-Detail.vue";
import NewProduct from "@/view/admin/admin-NewPro.vue";
import NewOrder from "@/view/buyer/buyer-Neworder.vue";
import SearchRes from "@/components/block-buyer/buyer-searchGoods.vue";
import NewLabel from "@/view/admin/admin-NewCat.vue";
import OrderDet from "@/components/block-admin/admin-order.vue";
import Collect from "@/view/buyer/buyer-Collect.vue";
import ShopCart from "@/view/buyer/buyer-ShoppingCart.vue";
import Pay from "@/view/buyer/buyer-Pay.vue";
const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/aHome',
    name: 'AHome',
    component: AHome,
    props: true // 启用 props
  },
  {
    path: '/aHome/updateGood/:pid',
    name: 'UpdateGood',
    component: UpdateGood,
    props: true // 启用 props
  },
  {
    path: '/aHome/newProduct',
    name: 'NewProduct',
    component: NewProduct,
    props: true // 启用 props
  },
  {
    path: '/aHome/newLabel',
    name: 'NewLabel',
    component: NewLabel,
    props: true // 启用 props
  },
  {
    path: '/aHome/productDet/:pid',
    name: 'AProductDet',
    component: AProductDet
  },
  {
    path: '/aHome/orderDet/:uid',
    name: 'OrderDet',
    component: OrderDet
  },
  {
    path: '/bHome',
    name: 'BHome',
    component: BHome,
    props: true // 启用 props
  },
  {
    path: '/bHome/newOrder/:id',
    name: 'NewOrder',
    component: NewOrder,
    props: true // 启用 props
  },
  {
    path: '/bHome/searchRes/:query',
    name: 'SearchRes',
    component: SearchRes,
    props: true // 启用 props
  },
  {
    path: '/bHome/collect',
    name: 'Collect',
    component: Collect,
    props: true // 启用 props
  },
  {
    path: '/bHome/pay',
    name: 'Pay',
    component: Pay,
    props: true // 启用 props
  },
  {
    path: '/bHome/productDet/:pid',
    name: 'BProductDet',
    component: BProductDet,
    props: true // 启用 props
  },
  {
    path: '/bHome/shopCart',
    name: 'ShopCart',
    component: ShopCart,
    props: true // 启用 props
  },
  {
    path: '/ingshell',
    name: 'Ishell',
    component: Ishell
  },
  {
    path: '/user-info',
    name: 'UserIn',
    component: UserIn
  },
  {
    path: '/updatePwd',
    name: 'UpdatePwd',
    component: UpdatePwd
  },
];
const router = createRouter({
  history: createWebHistory(),
  routes
})
export default router;
router.beforeEach((to, from, next) => {
//to到哪儿  from从哪儿离开  next跳转 为空就是放行
  if (to.path === '/') {
    //如果跳转为登录，就放行
    next();
  } else {
    //取出localStorage判断
    const token = localStorage.getItem('token');
    if (token === null || token === '') {
      alert('请先登录')
      next({path: '/'});
    } else {
      next();
    }
  }});
