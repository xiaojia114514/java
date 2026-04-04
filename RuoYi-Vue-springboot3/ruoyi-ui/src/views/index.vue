<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>若依后台管理框架</h2>
        <p>
          一直想做一款后台管理系统，看了很多优秀的开源项目但是发现没有合适自己的。于是利用空闲休息时间开始自己写一套后台系统。如此有了若依管理系统，她可以用于所有的Web应用程序，如网站管理后台，网站会员中心，CMS，CRM，OA等等，当然，您也可以对她进行深度定制，以做出更强系统。所有前端后台代码封装过后十分精简易上手，出错概率低。同时支持移动客户端访问。系统会陆续更新一些实用功能。
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Fastjson</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 30px">
      <el-col :span="24">
        <el-card class="notice-card">
          <div slot="header" class="clearfix">
            <span class="notice-card-title">
              <i class="el-icon-bell"></i> 通知公告
            </span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="viewAllNotices">查看全部</el-button>
          </div>
          <div v-if="noticeLoading" class="notice-loading">
            <i class="el-icon-loading"></i> 加载中...
          </div>
          <div v-else-if="noticeList.length === 0" class="notice-empty">
            <i class="el-icon-message"></i>
            <p>暂无通知</p>
          </div>
          <div v-else class="notice-list">
            <div 
              v-for="item in noticeList" 
              :key="item.noticeId" 
              class="notice-item"
              :class="'notice-level-' + (item.noticeLevel || 'normal')"
              @click="showNoticeDetail(item)"
            >
              <div class="notice-icon">
                <i :class="getNoticeIcon(item.noticeLevel)"></i>
              </div>
              <div class="notice-content">
                <div class="notice-header">
                  <span class="notice-title">{{ item.noticeTitle }}</span>
                  <el-tag 
                    size="mini" 
                    :type="getNoticeTagType(item.noticeLevel)"
                    effect="plain"
                  >{{ getNoticeLevelText(item.noticeLevel) }}</el-tag>
                </div>
                <div class="notice-text">{{ item.noticeContent | stripHtml }}</div>
                <div class="notice-time">
                  <i class="el-icon-time"></i> {{ item.createTime }}
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog 
      :title="currentNotice.noticeTitle" 
      :visible.sync="noticeDetailVisible" 
      width="600px"
      custom-class="notice-detail-dialog"
    >
      <div class="notice-detail-meta">
        <el-tag 
          size="small" 
          :type="getNoticeTagType(currentNotice.noticeLevel)"
        >{{ getNoticeLevelText(currentNotice.noticeLevel) }}</el-tag>
        <span class="notice-detail-time">
          <i class="el-icon-time"></i> {{ currentNotice.createTime }}
        </span>
      </div>
      <div class="notice-detail-divider"></div>
      <div class="notice-detail-content" v-html="currentNotice.noticeContent"></div>
    </el-dialog>
  </div>
</template>

<script>
import { listNotice } from "@/api/system/notice"

export default {
  name: "Index",
  data() {
    return {
      version: "3.9.1",
      noticeList: [],
      noticeLoading: false,
      noticeDetailVisible: false,
      currentNotice: {}
    }
  },
  created() {
    this.loadNotices()
  },
  filters: {
    stripHtml(html) {
      if (!html) return ''
      return html.replace(/<[^>]+>/g, '').substring(0, 100) + (html.length > 100 ? '...' : '')
    }
  },
  methods: {
    loadNotices() {
      this.noticeLoading = true
      listNotice({ pageNum: 1, pageSize: 5, status: '0' }).then(response => {
        this.noticeList = response.rows || []
      }).finally(() => {
        this.noticeLoading = false
      })
    },
    getNoticeIcon(level) {
      const icons = {
        'high': 'el-icon-warning',
        'medium': 'el-icon-info',
        'normal': 'el-icon-bell'
      }
      return icons[level] || icons['normal']
    },
    getNoticeTagType(level) {
      const types = {
        'high': 'danger',
        'medium': 'warning',
        'normal': 'info'
      }
      return types[level] || types['normal']
    },
    getNoticeLevelText(level) {
      const texts = {
        'high': '重要',
        'medium': '一般',
        'normal': '普通'
      }
      return texts[level] || texts['normal']
    },
    showNoticeDetail(notice) {
      this.currentNotice = notice
      this.noticeDetailVisible = true
    },
    viewAllNotices() {
      this.$router.push('/system/notice')
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}

.notice-card {
  .notice-card-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    
    i {
      margin-right: 8px;
      color: #409EFF;
    }
  }
  
  .notice-loading,
  .notice-empty {
    text-align: center;
    padding: 40px 0;
    color: #909399;
    
    i {
      font-size: 40px;
      margin-bottom: 10px;
    }
    
    p {
      margin: 0;
    }
  }
  
  .notice-list {
    .notice-item {
      display: flex;
      padding: 15px;
      margin-bottom: 12px;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s ease;
      border-left: 4px solid #409EFF;
      background: #f8fafc;
      
      &:hover {
        transform: translateX(5px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &.notice-level-high {
        border-left-color: #F56C6C;
        background: linear-gradient(to right, #fef0f0, #ffffff);
        
        .notice-icon i {
          color: #F56C6C;
        }
      }
      
      &.notice-level-medium {
        border-left-color: #E6A23C;
        background: linear-gradient(to right, #fdf6ec, #ffffff);
        
        .notice-icon i {
          color: #E6A23C;
        }
      }
      
      &.notice-level-normal {
        border-left-color: #409EFF;
        background: linear-gradient(to right, #ecf5ff, #ffffff);
        
        .notice-icon i {
          color: #409EFF;
        }
      }
      
      .notice-icon {
        flex-shrink: 0;
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        
        i {
          font-size: 18px;
        }
      }
      
      .notice-content {
        flex: 1;
        min-width: 0;
        
        .notice-header {
          display: flex;
          align-items: center;
          margin-bottom: 8px;
          
          .notice-title {
            font-size: 15px;
            font-weight: 600;
            color: #303133;
            margin-right: 10px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
        
        .notice-text {
          font-size: 13px;
          color: #606266;
          line-height: 1.6;
          margin-bottom: 8px;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        
        .notice-time {
          font-size: 12px;
          color: #909399;
          
          i {
            margin-right: 4px;
          }
        }
      }
    }
  }
}

.notice-detail-dialog {
  .notice-detail-meta {
    display: flex;
    align-items: center;
    gap: 15px;
    margin-bottom: 15px;
    
    .notice-detail-time {
      font-size: 13px;
      color: #909399;
      
      i {
        margin-right: 4px;
      }
    }
  }
  
  .notice-detail-divider {
    height: 1px;
    background: linear-gradient(to right, transparent, #e2e8f0, transparent);
    margin-bottom: 20px;
  }
  
  .notice-detail-content {
    font-size: 14px;
    line-height: 1.8;
    color: #303133;
    
    img {
      max-width: 100%;
      border-radius: 4px;
    }
    
    p {
      margin: 0 0 1em;
    }
    
    a {
      color: #409EFF;
      text-decoration: underline;
    }
  }
}
</style>
