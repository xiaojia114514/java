<template>
  <el-card class="address-management-card">
    <template #header>
      <div class="card-header">
        <span>{{ $t(isTranslated ? 'translate.menu.addressManagement_en' : 'translate.menu.addressManagement_zh') }}</span>
        <el-button type="primary" @click="showAddDialog">{{ $t(isTranslated ? 'order.actions.add_en' : 'order.actions.add_zh') }}</el-button>
      </div>
    </template>

    <el-table
      :data="addressList"
      stripe
      style="width: 100%"
      :row-class-name="tableRowClassName"
    >
      <el-table-column :prop="isTranslated ? 'atext' : 'atext'" :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')" min-width="200"/>
      <el-table-column :label="$t(isTranslated ? 'order.common.default_en' : 'order.common.default_zh')" width="120" align="right">
        <template #default="{ row }">
          <el-checkbox
            :model-value="row.adefault === 1"
            @change="(value) => toggleDefault(row, value)"
          />
        </template>
      </el-table-column>
      <el-table-column :label="$t(isTranslated ? 'order.operations_en' : 'order.operations_zh')" width="200" align="right">
        <template #default="{ row }">
          <el-button size="small" @click="showEditDialog(row)">{{ $t(isTranslated ? 'order.actions.edit_en' : 'order.actions.edit_zh') }}</el-button>
          <el-button type="danger" size="small" @click="deleteAddress(row.aid)">{{ $t(isTranslated ? 'order.actions.delete_en' : 'order.actions.delete_zh') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增地址对话框 -->
    <el-dialog
      v-model="addDialogVisible"
      :title="$t(isTranslated ? 'order.actions.add_en' : 'order.actions.add_zh')"
      width="500px"
    >
      <el-form
        ref="addressForm"
        :model="currentAddress"
        label-width="100px"
        :rules="formRules"
      >
        <el-form-item :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')" prop="atext">
          <el-input
            v-model="currentAddress.atext"
            :placeholder="$t(isTranslated ? 'order.address_en' : 'order.address_zh')"
          />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.common.default_en' : 'order.common.default_zh')">
          <el-checkbox v-model="currentAddress.adefault" :true-label="1" :false-label="0"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addDialogVisible = false">{{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}</el-button>
        <el-button type="primary" @click="submitAddress">{{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}</el-button>
      </template>
    </el-dialog>

    <!-- 编辑地址对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="$t(isTranslated ? 'order.actions.edit_en' : 'order.actions.edit_zh')"
      width="500px"
    >
      <el-form
        ref="editForm"
        :model="editingAddress"
        label-width="100px"
        :rules="formRules"
      >
        <el-form-item :label="$t(isTranslated ? 'order.address_en' : 'order.address_zh')" prop="atext">
          <el-input
            v-model="editingAddress.atext"
            :placeholder="$t(isTranslated ? 'order.address_en' : 'order.address_zh')"
          />
        </el-form-item>
        <el-form-item :label="$t(isTranslated ? 'order.common.default_en' : 'order.common.default_zh')">
          <el-checkbox v-model="editingAddress.adefault" :true-label="1" :false-label="0"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="editDialogVisible = false">{{ $t(isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh') }}</el-button>
        <el-button type="primary" @click="submitEdit">{{ $t(isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh') }}</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script>
import { defineComponent } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import { jwtDecode } from "jwt-decode"
import http from "@/utils/re.js"
import { useI18n } from 'vue-i18n'

const token = localStorage.getItem('token')

export default defineComponent({
  props: {
    isTranslated: {
      type: Boolean,
      default: false
    }
  },
  setup() {
    const { t } = useI18n()
    return { t }
  },
  data() {
    return {
      account: '',
      addressList: [],
      addDialogVisible: false,
      editDialogVisible: false,
      currentAddress: {
        atext: '',
        uaccount: '',
        adefault: 0
      },
      editingAddress: {
        aid: '',
        atext: '',
        uaccount: '',
        adefault: 0
      },
      formRules: {
        atext: [
          { required: true, message: this.t(this.isTranslated ? 'order.addressRequired_en' : 'order.addressRequired_zh'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.initAccount()
    this.fetchAddresses()
  },
  methods: {
    initAccount() {
      if (token) {
        const claims = jwtDecode(token)
        this.account = claims.account
        this.currentAddress.uaccount = claims.account
      } else {
        this.$router.push({ path: '/' })
      }
    },
    async fetchAddresses() {
      try {
        const data = {
          uaccount: this.account
        }
        const response = await http.post(`/address/list`, data ,{
          headers: { 'Authorization': token }
        });
        this.addressList = response.data
        this.sortAddresses()
      } catch (error) {
        const messageKey = this.isTranslated ? 'order.messages.error_en' : 'order.messages.error_zh'
        ElMessage.error(this.t(messageKey))
        console.error(error)
      }
    },
    showAddDialog() {
      this.currentAddress = {
        atext: '',
        uaccount: this.account,
        adefault: 0
      }
      this.addDialogVisible = true
    },
    showEditDialog(row) {
      this.editingAddress = {
        aid: row.aid,
        atext: row.atext,
        uaccount: this.account,
        adefault: row.adefault
      }
      this.editDialogVisible = true
    },
    async submitAddress() {
      try {
        if (this.currentAddress.adefault === 1) {
          await this.clearExistingDefault()
        }

        await http.post('/address/create', this.currentAddress, {
          headers: { 'Authorization': token }
        })
        ElMessage.success(this.t(this.isTranslated ? 'order.messages.addSuccess_en' : 'order.messages.addSuccess_zh'))
        this.addDialogVisible = false
        await this.fetchAddresses()
      } catch (error) {
        ElMessage.error(this.t(this.isTranslated ? 'order.messages.operationFailed_en' : 'order.messages.operationFailed_zh'))
        console.error(error)
      }
    },
    async submitEdit() {
      try {
        if (this.editingAddress.adefault === 1) {
          await this.clearExistingDefault()
        }

        await http.post('/address/update', this.editingAddress, {
          headers: { 'Authorization': token }
        })
        ElMessage.success(this.t(this.isTranslated ? 'order.messages.editSuccess_en' : 'order.messages.editSuccess_zh'))
        this.editDialogVisible = false
        await this.fetchAddresses()
      } catch (error) {
        ElMessage.error(this.t(this.isTranslated ? 'order.messages.operationFailed_en' : 'order.messages.operationFailed_zh'))
        console.error(error)
      }
    },
    async clearExistingDefault() {
      try {
        const response = await http.post('/address/list',
          { uaccount: this.account, adefault: 1 },
          { headers: { 'Authorization': token } }
        )

        if (response.data && response.data.length > 0) {
          const defaultAddress = response.data[0]
          await http.post('/address/update',
            { aid: defaultAddress.aid, adefault: 0 },
            { headers: { 'Authorization': token } }
          )
        }
      } catch (error) {
        console.error('清除默认地址失败:', error)
        throw error
      }
    },
    async toggleDefault(row, value) {
      try {
        if (value) {
          await this.clearExistingDefault()
        }

        const updatedAddress = {
          aid: row.aid,
          adefault: value ? 1 : 0
        }
        await http.post('/address/update', updatedAddress, {
          headers: {'Authorization': token}
        })
        ElMessage.success(this.t(this.isTranslated ? 'order.messages.defaultUpdated_en' : 'order.messages.defaultUpdated_zh'))
        await this.fetchAddresses()
      } catch (error) {
        ElMessage.error(this.t(this.isTranslated ? 'order.messages.operationFailed_en' : 'order.messages.operationFailed_zh'))
        console.error(error)
      }
    },
    async deleteAddress(aid) {
      try {
        await ElMessageBox.confirm(this.t(this.isTranslated ? 'order.messages.deleteConfirm_en' : 'order.messages.deleteConfirm_zh'), this.t(this.isTranslated ? 'order.messages.warning_en' : 'order.messages.warning_zh'), {
          confirmButtonText: this.t(this.isTranslated ? 'order.actions.submit_en' : 'order.actions.submit_zh'),
          cancelButtonText: this.t(this.isTranslated ? 'order.actions.cancel_en' : 'order.actions.cancel_zh'),
          type: 'warning'
        })

        await http.delete(`/address/delete/${aid}`, {
          headers: {'Authorization': token}
        })
        ElMessage.success(this.t(this.isTranslated ? 'order.messages.deleteSuccess_en' : 'order.messages.deleteSuccess_zh'))
        await this.fetchAddresses()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error(this.t(this.isTranslated ? 'order.messages.deleteFailed_en' : 'order.messages.deleteFailed_zh'))
          console.error(error)
        }
      }
    },
    sortAddresses() {
      this.addressList.sort((a, b) => {
        if (a.adefault === 1) return -1
        if (b.adefault === 1) return 1
        return 0
      })
    }
  }
})
</script>

<style scoped>
.address-management-card {
  max-width: 800px;
  margin: 20px auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
}

.el-table {
  margin-top: 15px;
}

:deep(.el-table__row) .el-checkbox {
  margin-right: 8px;
}

:deep(.el-table__row) .el-checkbox.is-disabled {
  cursor: not-allowed;
}
</style>
