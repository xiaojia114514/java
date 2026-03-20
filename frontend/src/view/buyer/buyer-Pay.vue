<template>
  <buyer-header :is-translated="isTranslated"></buyer-header>
  <div class="payment-container">
    <!-- 总金额显示区域 -->
    <div class="total-amount-container">
      <h3>{{ $t(isTranslated ? 'order.total-value_en' : 'order.total-value_zh' ) }}</h3>
      <div class="amount-display">
        <span class="currency">¥</span>
        <span class="total-value">{{ formattedTotalAmount }}</span>
      </div>
    </div>

    <h2>{{ $t(isTranslated ? 'translate.payment.selectPaymentMethod_en' : 'translate.payment.selectPaymentMethod_zh' ) }}</h2>
    <div class="payment-options">
      <!-- 支付宝支付选项 -->
      <div
        class="payment-item"
        :class="{ 'selected': selectedMethod === 'alipay' }"
        @click="selectMethod('alipay')"
      >
        <div class="checkmark">
          <input
            type="radio"
            id="alipay"
            value="alipay"
            v-model="selectedMethod"
            hidden
          />
          <span class="custom-checkmark" v-show="selectedMethod === 'alipay'">✓</span>
        </div>
        <img :src="alipayIcon" :alt="$t(isTranslated ? 'translate.payment.alipay_en' : 'translate.payment.alipay_zh' )" class="payment-icon" />
        <div class="payment-info">
          <h3>{{ $t(isTranslated ? 'translate.payment.alipay_en' : 'translate.payment.alipay_zh' ) }}</h3>
          <p>{{ $t(isTranslated ? 'translate.payment.alipayDescription_en' : 'translate.payment.alipayDescription_zh' ) }}</p>
          <p class="method-amount">
            {{ $t(isTranslated ? 'translate.payment.amountDue_en' : 'translate.payment.amountDue_zh' ) }}: ¥{{ formattedTotalAmount }}
          </p>
        </div>
      </div>

      <!-- Stripe支付选项 -->
      <div
        class="payment-item"
        :class="{ 'selected': selectedMethod === 'stripe' }"
        @click="selectMethod('stripe')"
      >
        <div class="checkmark">
          <input
            type="radio"
            id="stripe"
            value="stripe"
            v-model="selectedMethod"
            hidden
          />
          <span class="custom-checkmark" v-show="selectedMethod === 'stripe'">✓</span>
        </div>
        <img :src="stripeIcon" :alt="$t(isTranslated ? 'translate.payment.creditCard_en' : 'translate.payment.creditCard_zh' )" class="payment-icon" />
        <div class="payment-info">
          <h3>{{ $t(isTranslated ? 'translate.payment.creditCard_en' : 'translate.payment.creditCard_zh' ) }}</h3>
          <p>{{ $t(isTranslated ? 'translate.payment.creditCardDescription_en' : 'translate.payment.creditCardDescription_zh' ) }}</p>
          <p class="method-amount">
            {{ $t(isTranslated ? 'translate.payment.amountDue_en' : 'translate.payment.amountDue_zh' ) }}: ¥{{ formattedTotalAmount }}
          </p>
        </div>
      </div>
    </div>

    <!-- Stripe支付表单 (仅在选择Stripe时显示) -->
    <div v-if="selectedMethod === 'stripe'" class="stripe-form-container">
      <div id="card-element" class="stripe-element"></div>
      <div id="card-errors" class="error-message"></div>
    </div>

    <!-- 按钮组 -->
    <div class="button-group">
      <button
        class="pay-button"
        @click="handlePayment"
        :disabled="!selectedMethod || (selectedMethod === 'stripe' && stripeProcessing)"
      >
        {{ stripeProcessing ?
        $t(isTranslated ? 'translate.payment.processing_en' : 'translate.payment.processing_zh') :
        $t(isTranslated ? 'translate.payment.payNow_en' : 'translate.payment.payNow_zh')
        }}
      </button>
      <button
        class="cancel-button"
        @click="cancelPayment"
      >
        {{ $t(isTranslated ? 'translate.payment.cancelPayment_en' : 'translate.payment.cancelPayment_zh') }}
      </button>
    </div>
  </div>
  <TranslateButton
    v-model:isTranslated="isTranslated"
    class="floating-translate-btn"
  />
</template>

<script setup>
import {ref, reactive, onMounted, computed, nextTick} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {jwtDecode} from 'jwt-decode';
import axios from 'axios';
import {loadStripe} from '@stripe/stripe-js';
import alipayIcon from '@/assets/alipay-icon.png';
import bankcardIcon from '@/assets/bankcard-icon.png';
import stripeIcon from '@/assets/bankcard-icon.png'; // 需要添加Stripe图标
import BuyerHeader from '@/components/block-buyer/buyer-header.vue';
import http from "@/utils/re.js";
import TranslateButton from "@/components/block/translate.vue";
import { useI18n } from 'vue-i18n';
// 声明变量
const paymentData = ref(null);
const token = localStorage.getItem('token');
const claims = token ? jwtDecode(token) : {};
const route = useRoute();
const router = useRouter();
const selectedMethod = ref(null);
const stripe = ref(null);
const cardElement = ref(null);
const stripeProcessing = ref(false);
const isTranslated = ref(false);
// Stripe相关元素
const stripeElements = ref(null);
const errorElement = ref(null);

// 格式化总金额
const formattedTotalAmount = computed(() => {
  if (!paymentData.value || !paymentData.value.totalAmount) return '0.00';
  return parseFloat(paymentData.value.totalAmount).toFixed(2);
});

// 选择支付方式
const selectMethod = async (methodId) => {
  selectedMethod.value = methodId;
  if (methodId === 'stripe') {
    await initStripe(); // 等待初始化完成
  }
};
// 初始化Stripe
const initStripe = async () => {
  try {
    // 确保Stripe实例存在
    if (!stripe.value) {
      stripe.value = await loadStripe('pk_test_51RBDgVRvCyjdHkjBEwCNF85xp6j2tOUyWUjlkKiNrwIqPzUzLZ8dEJX2ceri64b24MAjtMiKtISyKMaDBzveZ7VY00u8ctyaTz');
    }

    // 等待DOM更新确保元素存在
    await nextTick();

    // 清理旧的Elements实例
    if (stripeElements.value) {
      stripeElements.value = null;
    }
    stripeElements.value = stripe.value.elements();

    // 清理旧的card元素
    if (cardElement.value) {
      cardElement.value.unmount();
      cardElement.value = null;
    }

    // 创建新的card元素
    const style = {
      base: {
        fontSize: '16px',
        color: '#32325d',
        fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
        '::placeholder': {
          color: '#aab7c4'
        }
      },
      invalid: {
        color: '#fa755a',
        iconColor: '#fa755a'
      }
    };

    cardElement.value = stripeElements.value.create('card', {style});
    cardElement.value.mount('#card-element');

    // 初始化错误处理
    errorElement.value = document.getElementById('card-errors');
    cardElement.value.on('change', (event) => {
      errorElement.value.textContent = event.error ? event.error.message : '';
    });
  } catch (error) {
    console.error('Stripe初始化失败:', error);
  }
};

// 处理支付
const handlePayment = async () => {
  if (!selectedMethod.value) return;

  try {
    if (selectedMethod.value === 'alipay') {
      await handleAlipay();
    } else if (selectedMethod.value === 'stripe') {
      await handleStripePayment();
    }
  } catch (error) {
    console.error('支付失败:', error);
    alert('支付失败，请重试: ' + error.message);
  }
};

// 处理支付宝支付
const handleAlipay = async () => {
  console.log('Calling Alipay API with data:', paymentData.value);
  const response = await http.post('/alipay/pay', paymentData.value, {
    headers: {
      'Authorization': `${token}`,
    }
  });

  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = response.data;
  const form = tempDiv.querySelector('form');
  const script = tempDiv.querySelector('script');

  if (form && script) {
    document.body.appendChild(form);
    eval(script.textContent);
  } else {
    throw new Error('支付宝返回的支付表单格式不正确');
  }
};
// 验证支付状态函数
const verifyPaymentStatus = async (paymentIntentId) => {
  try {
    const response = await http.get(
      `/stripe/payment-status/${paymentIntentId}`,
      {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      }
    );
    return response.data; // 返回支付状态字符串
  } catch (error) {
    console.error("支付验证失败:", error);
    throw error;
  }
};
// 处理Stripe支付
const handleStripePayment = async () => {
  if (!stripe.value || !cardElement.value) {
    throw new Error('Stripe未正确初始化');
  }
  stripeProcessing.value = true;
  try {
    // 创建支付意向
    const response = await http.post('/stripe/create-payment-intent', {
      amount: 1000, // 从支付数据中获取金额
      currency: 'CNY',
      orderId: paymentData.value.oid   // 传递订单ID
    },);
    const clientSecret = response.data.clientSecret;
    // 确认支付
    const {error, paymentIntent} = await stripe.value.confirmCardPayment(clientSecret, {
      payment_method: {
        card: cardElement.value,
        billing_details: {
          name: 'Jenny Rosen',
        },
      },
    });
    if (error) throw error;
    // 3. 验证支付状态
    let paymentStatus = await verifyPaymentStatus(paymentIntent.id);
    console.log(paymentStatus);
    let retries = 0;
    // 轮询检查支付状态（最多5次，每次间隔2秒）
    while (paymentStatus !== 'succeeded' && retries < 5) {
      await new Promise(resolve => setTimeout(resolve, 2000));
      paymentStatus = await verifyPaymentStatus(paymentIntent.id);
      retries++;
    }
    // 4. 处理最终结果
    if (paymentStatus === 'succeeded') {
      // 更新本地订单状态
      await router.push('/user-info?status=success');
    } else {
      throw new Error(`支付未完成，最终状态: ${paymentStatus}`);
    }
  } catch (error) {
    console.error("支付流程错误:", error);
    alert(`支付失败: ${error.message}`);
  } finally {
    stripeProcessing.value = false;
  }
};

// 取消支付
const cancelPayment = () => {
  router.push({
    path: '/user-info',
    query: { status: 'cancel' }
  });
};

// 组件挂载时获取路由传递的数据
onMounted(() => {
  paymentData.value = {
    oid: route.query.oid,
    totalAmount: route.query.totalAmount,
    subject: route.query.subject
  };
  console.log("获得数据：", paymentData.value);
});
</script>

<style scoped>
.payment-container {
  max-width: 500px;
  margin: 20px auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.total-amount-container {
  background-color: #f5faff;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
  text-align: center;
  border: 1px solid #e0e0e0;
}

.total-amount-container h3 {
  margin: 0 0 10px;
  color: #666;
  font-size: 14px;
}

.amount-display {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.currency {
  color: #409eff;
  font-size: 16px;
  vertical-align: super;
}

.total-value {
  color: #ff6b6b;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.payment-options {
  margin-bottom: 30px;
}

.payment-item {
  display: flex;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-item:hover {
  border-color: #409eff;
}

.payment-item.selected {
  border-color: #409eff;
  background-color: #f5faff;
}

.checkmark {
  margin-right: 15px;
  position: relative;
  width: 20px;
  height: 20px;
}

.custom-checkmark {
  position: absolute;
  top: -5px;
  left: 0;
  width: 20px;
  height: 20px;
  background-color: #409eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.payment-icon {
  width: 40px;
  height: 40px;
  margin-right: 15px;
}

.payment-info h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.payment-info p {
  margin: 5px 0 0;
  color: #666;
  font-size: 14px;
}

.method-amount {
  margin-top: 8px;
  color: #ff6b6b;
  font-weight: bold;
}

/* Stripe表单样式 */
.stripe-form-container {
  margin: 20px 0;
}

.stripe-element {
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  margin-bottom: 10px;
}

.error-message {
  color: #fa755a;
  margin: 10px 0;
  min-height: 20px;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  gap: 10px;
  justify-content: space-between;
}

.pay-button {
  width: 48%;
  padding: 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pay-button:hover {
  background-color: #66b1ff;
}

.pay-button:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
}

.cancel-button {
  width: 48%;
  padding: 12px;
  background-color: #fff;
  color: #666;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-button:hover {
  background-color: #f5f5f5;
  border-color: #409eff;
  color: #409eff;
}
</style>
