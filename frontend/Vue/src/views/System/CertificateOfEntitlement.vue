<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-bank-card"></i> 权益存证
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-select v-model="query.type" clearable placeholder="作品分类" @change="changeSelect" class="handle-select mr10">
          <el-option
              v-for="item in workTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
        <el-input v-model="query.worknname" :clearable="true" placeholder="搜索作品名称" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAdd">权益存证</el-button>
        <el-tooltip placement="top">
          <div slot="content">
            请勿重复添加权益存证文件，否则会导致上链失败<br/>
          </div>
          <i class="el-icon-question"></i>>
        </el-tooltip>
      </div>

      <el-table
          v-loading="loading"
          :data="tableData"
          border
          class="table"
          ref="multipleTable"
          header-cell-class-name="table-header"
      >
        <el-table-column prop="workname" label="作品名称"  align="center"></el-table-column>
        <el-table-column prop="type" label="作品类别"  align="center">
          <template slot-scope="scope">
            <p v-if="scope.row.type===1">摄影作品</p>
            <p v-if="scope.row.type===2">文字作品</p>
            <p v-if="scope.row.type===3">影视作品</p>
            <p v-if="scope.row.type===4">音乐作品</p>
            <p v-if="scope.row.type===5">美术作品</p>
          </template>
        </el-table-column>
        <el-table-column  label="创建时间" align="center">
          <template slot-scope="scope">
            <span>{{scope.row.createtime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="workfile" label="存证文件" align="center" >
          <template slot-scope="scope">
            <el-link
                type="primary"
                :underline="false"
                :href="scope.row.filedownloadurl"
            >
              {{ scope.row.filename }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <el-tag
                :type="scope.row.status===1?'success':(scope.row.status===0?'danger':'info')"
            >{{scope.row.status===0?'未存证':scope.row.status===1?'已存证':'已转让'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
                type="text"
                icon="el-icon-edit"
                :disabled="scope.row.status===1||scope.row.status===2"
                @click="handleEdit(scope.$index, scope.row)"
            >编辑</el-button>
            <el-button
                type="text"
                icon="el-icon-delete"
                :disabled="scope.row.status===1||scope.row.status===2"
                @click="handleDelete(scope.$index, scope.row)"
            >删除</el-button>
            <el-button :disabled="scope.row.status===1||scope.row.status===2"
                type="text"
                icon="el-icon-upload2"
                @click="handleConflux(scope.$index, scope.row)"
            >上链存证</el-button>
            <el-button
                type="text"
                icon="el-icon-reading"
                :disabled="scope.row.status===0"
                @click="handleDetail(scope.$index, scope.row)"
            >查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :current-page="query.pageIndex"
            :page-size="query.pageSize"
            :total="pageTotal"
            @current-change="handlePageChange">
        </el-pagination>
      </div>
    </div>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑作品信息" :visible.sync="editVisible" width="50%" :before-close="handleEditDialogClose" @close="closeEditDialog('editForm')">
      <el-form ref="editForm" :model="editForm" :rules="editRules" label-width="auto" label-position="top"  >
        <div>
          <el-card class="box-card1">
            <div slot="header" class="clearfix">
              <span>作品信息</span>
            </div>
            <div>
              <el-row>
                <el-col :span="10">
                  <el-form-item  label="作品名称" prop="workname" >
                    <el-input v-model.trim="editForm.workname" placeholder="请输入作品名称"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" style="padding-left: 20px">
                  <el-form-item prop="type" label="作品分类" >
                    <el-select v-model="editForm.type" clearable placeholder="请选择" >
                      <el-option
                          v-for="item in workTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item prop="introduction" label="作品简介">
                <el-input placeholder="请输入作品简介"  type="textarea" v-model.trim="editForm.introduction"></el-input>
              </el-form-item>
            </div>
          </el-card>
        </div>

        <div>
          <el-card class="box-card2" style="margin-top: 10px">
            <div slot="header" class="clearfix">
              <span>存证信息</span>
            </div>
            <div>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="存证主体" >
                    <el-select v-model="this.userinfo.realname" :disabled="true"  ></el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item label="身份证" >
                    <el-input v-model="userinfo.idnumber" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item prop="copyrightownertype" label="著作权人类别"  >
                    <el-select :clearable="true" v-model="editForm.copyrightownertype" @change="ownerTypeChange">
                      <el-option
                          v-for="item in ownerTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item prop="copyrightownername" label="名称" >
                    <el-input v-model.trim="editForm.copyrightownername" placeholder="姓名或名称，与身份证文件保持一致"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="证件类型" prop="idtype" >
                    <el-select :clearable="true" v-model="editForm.idtype">
                      <el-option
                          v-for="item in idTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item label="证件号码" prop="copyrightownerid" >
                    <el-input v-model.trim="editForm.copyrightownerid" placeholder="请输入证件号码"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-tooltip placement="top">
                <div slot="content">
                  存证主体:指当前操作存证的自然人或法人。<br/>
                  著作权人:指享有作品著作权的主体。<br/>
                  一般情况下存证主体即为著作权人，不过著作权人也可以委托第三方机构代为办理存证，此时存证主体为第三方代理机构。
                </div>
                <i class="el-icon-question">存证主体和著作权人</i>>
              </el-tooltip>
            </div>
          </el-card>
        </div>

        <div>
          <el-card class="box-card3" style="margin-top: 10px">
            <div slot="header" class="clearfix">
              <span>存证文件</span>
            </div>
            <div>
              <el-alert
                  title="特别提醒：严禁上传他人著作权作品、以及违法、反动、危害社会公共安全等不良信息。上传后会覆盖原来的文件。"
                  type="warning"
                  effect="dark" :closable="false">
              </el-alert>
              <el-upload class="upload-demo"  style="width: 100%;height: 100%"  drag  :on-preview="handlePreview1"
                         action="TODO"
                         :on-remove="handleRemove1"
                         :before-remove="beforeRemove1"
                         :limit="1"
                         :on-exceed="handleExceed1"
                         :on-change="handleChange1"
                         :file-list="fileList1"
                         :auto-upload="false"
                         :show-file-list="true"
                         :before-upload="onBeforeUpload"
                         ref="uploadComponent1">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                  将文件拖到此处，或
                  <em>点击上传</em>
                </div>
                <template #tip>
                  <div class="el-upload__tip">只能上传单个文件，多个文件请上传压缩包，单文件上传大小1GB以内，支持各种图片、视频、文本、音频、压缩包等文件类型上传，暂不支持php、jsp、asp、aspx、exe、msi、bat等文件类型</div>
                </template>
              </el-upload>
            </div>
          </el-card>
        </div>

        <div>
          <el-card class="box-card4" style="margin-top: 10px">
            <div slot="header" class="clearfix">
              <span>权利声明(可选)  </span>
              <el-switch
                  v-model="editForm.isauthority"
                  :active-value=true
                  :inactive-value=false
                  active-color="#13ce66"
                  inactive-color="#ff4949">
              </el-switch>
            </div>
            <div>
              <el-alert
                  title="权利声明作为权利人对自己创作或获得许可作品权利的一种书面主张。在被侵权需主张自己的权利时，声明的事项可作为拥有权利的初步证明,以下内容请如实填写."
                  type="info"
                  effect="dark" :closable="false">
              </el-alert>
            </div>

            <div style="margin-top: 10px" v-if="editForm.isauthority">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="创作完成时间" prop="completiontime" >
                    <el-date-picker
                        v-model="editForm.completiontime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期时间"
                        align="right"
                        :picker-options="pickerOptions">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item label="创作完成地点" prop="completionlocation" >
                    <el-input v-model.trim="editForm.completionlocation" placeholder="请输入具体地理位置，精确到城市即可"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="存证文件是否已发表" prop="ispublished" >
                <el-radio v-model="editForm.ispublished" :label="true">已发表</el-radio>
                <el-radio v-model="editForm.ispublished" :label="false">未发表</el-radio>
              </el-form-item>
              <div v-if="editForm.ispublished===true">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="发表时间" prop="publishedtime" key="vif1" >
                      <el-date-picker
                          v-model="editForm.publishedtime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期"
                          align="right"
                          :picker-options="pickerOptions">
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" >
                    <el-form-item label="发表地点" prop="publishedlocation"  key="vif2">
                      <el-input v-model="editForm.publishedlocation" placeholder="发表的线上或线下平台名称"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <el-form-item label="当前操作人是否为作者" prop="isauthor" >
                <el-radio v-model="editForm.isauthor" :label="true">是</el-radio>
                <el-radio v-model="editForm.isauthor" :label="false">否</el-radio>
              </el-form-item>
              <div v-if="editForm.isauthor===true">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="作者"  key="vif3" >
                      <el-input v-model="this.userinfo.realname" placeholder="作者姓名" :disabled="true" ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" >
                    <el-form-item label="作者身份证号码"  key="vif4" >
                      <el-input v-model="this.userinfo.idnumber" placeholder="作者身份证号" :disabled="true"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <div v-if="editForm.isauthor===false">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="作者" prop="authorname"  key="vif5" >
                      <el-input v-model.trim="editForm.authorname" placeholder="作者姓名"  ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" >
                    <el-form-item label="作者身份证号码" prop="authoridnumber"  key="vif6">
                      <el-input v-model.trim="editForm.authoridnumber" placeholder="作者身份证号" ></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <el-tooltip placement="top">
                <div slot="content">
                  创作作品的公民是作者。<br/>
                  由法人或其他组织主持，代表法人或其他组织意志创作，并有法人或者其他组织承担责任的作品，法人或其他组织视为作者.<br/>
                  一般作者就是著作权人，但是在发生著作权继承，交易等情况下，著作权人就发生了转移.
                </div>
                <i class="el-icon-question">著作权人和作者</i>>
              </el-tooltip>
            </div>
          </el-card>
        </div>
      </el-form>
<!--      <el-form ref="editForm" :model="editForm" :rules="editRules" label-width="auto" label-position="top">-->
<!--        <el-form-item prop="workname" label="作品名称">-->
<!--          <el-input :disabled="true" v-model="editForm.workname"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="type" label="作品分类">-->
<!--          <el-select v-model="editForm.type" :disabled="true" >-->
<!--            <el-option label="摄影作品" :value="1"/>-->
<!--            <el-option label="文字作品" :value="2"/>-->
<!--            <el-option label="影视作品" :value="3"/>-->
<!--            <el-option label="音乐作品" :value="4"/>-->
<!--            <el-option label="美术作品" :value="5"/>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="introduction" label="作品简介">-->
<!--          <el-input placeholder="请输入作品简介"  type="textarea" v-model="editForm.introduction"></el-input>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
      <span slot="footer" class="dialog-footer-edit">
        <el-button @click="editVisible=false">取 消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </span>
    </el-dialog>

    <!-- 添加弹出框 -->
    <el-dialog title="新增权益存证" :visible.sync="addVisible" width="50%" :before-close="handleAddDialogClose" @close="closeAddDialog('addForm')" >
      <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="auto" label-position="top"  >
        <div>
          <el-card class="box-card1">
            <div slot="header" class="clearfix">
              <span>作品信息</span>
            </div>
            <div>
              <el-row>
                <el-col :span="10">
                  <el-form-item  label="作品名称" prop="workname" >
                    <el-input v-model.trim="addForm.workname" placeholder="请输入作品名称"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" style="padding-left: 20px">
                  <el-form-item prop="type" label="作品分类" >
                    <el-select v-model="addForm.type" clearable placeholder="请选择" >
                      <el-option
                          v-for="item in workTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item prop="introduction" label="作品简介">
                <el-input placeholder="请输入作品简介"  type="textarea" v-model.trim="addForm.introduction"></el-input>
              </el-form-item>
            </div>
          </el-card>
        </div>

        <div>
          <el-card class="box-card2" style="margin-top: 10px">
            <div slot="header" class="clearfix">
              <span>存证信息</span>
            </div>
            <div>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="存证主体" >
                    <el-select v-model="this.userinfo.realname" :disabled="true"  ></el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item label="身份证" >
                    <el-input v-model="userinfo.idnumber" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item prop="copyrightownertype" label="著作权人类别"  >
                    <el-select :clearable="true" v-model="addForm.copyrightownertype" @change="ownerTypeChange">
                      <el-option
                          v-for="item in ownerTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item prop="copyrightownername" label="名称" >
                    <el-input v-model.trim="addForm.copyrightownername" placeholder="姓名或名称，与身份证文件保持一致"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="证件类型" prop="idtype" >
                    <el-select :clearable="true" v-model="addForm.idtype">
                      <el-option
                          v-for="item in idTypeOptions"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item label="证件号码" prop="copyrightownerid" >
                    <el-input v-model.trim="addForm.copyrightownerid" placeholder="请输入证件号码"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-tooltip placement="top">
                <div slot="content">
                  存证主体:指当前操作存证的自然人或法人。<br/>
                  著作权人:指享有作品著作权的主体。<br/>
                  一般情况下存证主体即为著作权人，不过著作权人也可以委托第三方机构代为办理存证，此时存证主体为第三方代理机构。
                </div>
                <i class="el-icon-question">存证主体和著作权人</i>>
              </el-tooltip>
            </div>
          </el-card>
        </div>

        <div>
          <el-card class="box-card3" style="margin-top: 10px">
            <div slot="header" class="clearfix">
              <span>存证文件</span>
            </div>
            <div>
              <el-alert
                  title="特别提醒：严禁上传他人著作权作品、以及违法、反动、危害社会公共安全等不良信息。"
                  type="warning"
                  effect="dark" :closable="false">
              </el-alert>
              <el-upload class="upload-demo"  style="width: 100%;height: 100%"  drag  :on-preview="handlePreview"
                         action="TODO"
                         :on-remove="handleRemove"
                         :before-remove="beforeRemove"
                         :limit="1"
                         :on-exceed="handleExceed"
                         :on-change="handleChange"
                         :file-list="fileList"
                         :auto-upload="false"
                         :show-file-list="true"
                         :before-upload="onBeforeUpload"
                         ref="uploadComponent">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                  将文件拖到此处，或
                  <em>点击上传</em>
                </div>
                <template #tip>
                  <div class="el-upload__tip">只能上传单个文件，多个文件请上传压缩包，单文件上传大小1GB以内，支持各种图片、视频、文本、音频、压缩包等文件类型上传，暂不支持php、jsp、asp、aspx、exe、msi、bat等文件类型</div>
                </template>
              </el-upload>
            </div>
          </el-card>
        </div>

        <div>
          <el-card class="box-card4" style="margin-top: 10px">
            <div slot="header" class="clearfix">
              <span>权利声明(可选)  </span>
              <el-switch
                  v-model="addForm.isauthority"
                  :active-value=true
                  :inactive-value=false
                  active-color="#13ce66"
                  inactive-color="#ff4949">
              </el-switch>
            </div>
            <div>
              <el-alert
                  title="权利声明作为权利人对自己创作或获得许可作品权利的一种书面主张。在被侵权需主张自己的权利时，声明的事项可作为拥有权利的初步证明,以下内容请如实填写."
                  type="info"
                  effect="dark" :closable="false">
              </el-alert>
            </div>

            <div style="margin-top: 10px" v-if="addForm.isauthority">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="创作完成时间" prop="completiontime" >
                    <el-date-picker
                        v-model="addForm.completiontime"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期时间"
                        align="right"
                        :picker-options="pickerOptions">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="10" >
                  <el-form-item label="创作完成地点" prop="completionlocation" >
                    <el-input v-model.trim="addForm.completionlocation" placeholder="请输入具体地理位置，精确到城市即可"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="存证文件是否已发表" prop="ispublished" >
                <el-radio v-model="addForm.ispublished" :label="true">已发表</el-radio>
                <el-radio v-model="addForm.ispublished" :label="false">未发表</el-radio>
              </el-form-item>
              <div v-if="addForm.ispublished===true">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="发表时间" prop="publishedtime" key="vif1" >
                      <el-date-picker
                          v-model="addForm.publishedtime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期"
                          align="right"
                          :picker-options="pickerOptions">
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" >
                    <el-form-item label="发表地点" prop="publishedlocation"  key="vif2">
                      <el-input v-model="addForm.publishedlocation" placeholder="发表的线上或线下平台名称"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <el-form-item label="当前操作人是否为作者" prop="isauthor" >
                <el-radio v-model="addForm.isauthor" :label="true">是</el-radio>
                <el-radio v-model="addForm.isauthor" :label="false">否</el-radio>
              </el-form-item>
              <div v-if="addForm.isauthor===true">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="作者"  key="vif3" >
                      <el-input v-model="this.userinfo.realname" placeholder="作者姓名" :disabled="true" ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" >
                    <el-form-item label="作者身份证号码"  key="vif4" >
                      <el-input v-model="this.userinfo.idnumber" placeholder="作者身份证号" :disabled="true"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <div v-if="addForm.isauthor===false">
                <el-row >
                  <el-col :span="10">
                    <el-form-item label="作者" prop="authorname"  key="vif5" >
                      <el-input v-model.trim="addForm.authorname" placeholder="作者姓名"  ></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" >
                    <el-form-item label="作者身份证号码" prop="authoridnumber"  key="vif6">
                      <el-input v-model.trim="addForm.authoridnumber" placeholder="作者身份证号" ></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
              <el-tooltip placement="top">
                <div slot="content">
                  创作作品的公民是作者。<br/>
                  由法人或其他组织主持，代表法人或其他组织意志创作，并有法人或者其他组织承担责任的作品，法人或其他组织视为作者.<br/>
                  一般作者就是著作权人，但是在发生著作权继承，交易等情况下，著作权人就发生了转移.
                </div>
                <i class="el-icon-question">著作权人和作者</i>>
              </el-tooltip>
            </div>
          </el-card>
        </div>
      </el-form>
      <div slot="footer"  style="margin-top: 1% " class="dialog-footer-add">
        <el-button @click="addVisible=false">取 消</el-button>
        <el-button type="primary" v-loading.fullscreen.lock="addDialogLoading" @click="saveAdd">立即存证</el-button>
      </div>
    </el-dialog>

    <el-drawer
        title="查看详情"
        :visible.sync="drawer"
        direction="rtl"
        size="50%"
        :before-close="handleCloseDrawer">
      <el-tabs type="border-card">
        <el-tab-pane label="权益存证详情">
          <div>
            <el-card>
              <div slot="header" class="clearfix">
                <span>存证信息</span>
              </div>
              <div>
                <el-row :gutter="2">
                  <el-col :span="5">
                    <span >存证主体：{{this.userinfo.realname}}</span>
                  </el-col>
                  <el-col :span="12">
                    <span >存证主体证件号码：{{this.userinfo.idnumber}}</span>
                  </el-col>
                </el-row>
                <el-row :gutter="2" style="margin-top: 20px">
                  <el-col :span="5">
                    <span >著作权人：{{this.drawerMessage.copyrightownername}}</span>
                  </el-col>
                  <el-col :span="12">
                    <span >著作权人证件号码：{{this.drawerMessage.copyrightownerid}}</span>
                  </el-col>
                </el-row>
                <el-row :gutter="2" style="margin-top: 20px">
                  <el-col :span="20">
                    <span >交易哈希：{{this.drawerMessage.chainhash}}</span>
                  </el-col>
                </el-row>
                <el-row :gutter="2" style="margin-top: 20px">
                  <el-col :span="20">
                    <span >文件哈希：{{this.drawerMessage.filehash}}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </div>
          <div style="margin-top: 20px" v-if="drawerMessage.isauthority">
            <el-card >
              <div slot="header" class="clearfix">
                <span>权利声明</span>
              </div>
              <div>
                <el-row :gutter="2">
                  <el-col :span="8">
                    <span >创作完成时间：{{this.drawerMessage.completiontime}}</span>
                  </el-col>
                  <el-col :span="8">
                    <span >创作完成地点：{{this.drawerMessage.completionlocation}}</span>
                  </el-col>
                  <el-col :span="8">
                    <span >存证文件是否已发表：{{this.drawerMessage.ispublished===true?'是':'否'}}</span>
                  </el-col>
                </el-row>
                <el-row :gutter="2" style="margin-top: 20px">
                  <el-col :span="8">
                    <span >发表时间：{{this.drawerMessage.publishedtime}}</span>
                  </el-col>
                  <el-col :span="8">
                    <span >发表地点：{{this.drawerMessage.publishedlocation}}</span>
                  </el-col>
                </el-row>
                <el-row :gutter="2" style="margin-top: 20px">
                  <el-col :span="8">
                    <span >作者：{{this.drawerMessage.authorname}}</span>
                  </el-col>
                  <el-col :span="8">
                    <span >作者证件号码：{{this.drawerMessage.authoridnumber}}</span>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </div>
        </el-tab-pane>

        <el-tab-pane label="权益存证证明">
          <div>
            <el-card style="height: 50%">
              <div slot="header" class="clearfix">
                <span>在线预览</span>
                <el-link  :disabled="this.drawerMessage.certificatefilelocation===null" style="float: right; padding: 3px 0" :href="this.drawerMessage.certificatefilelocation" type="primary">下载证书</el-link>
              </div>
            </el-card>
          </div>
          <pdf
              ref="pdf"
              :src="this.drawerMessage.certificatefilelocation">
          </pdf>
        </el-tab-pane>
      </el-tabs>
    </el-drawer>
  </div>
</template>

<script>

import { isIdCard } from '@/utils/validate'
import confluxPortal from '../../conflux/conflux-portal'
import conflux from '../../conflux/conflux'
import pdf from 'vue-pdf'
const {ContractHnuCoin,ContractCopyrightSystemTransactions,ContractCopyright} = require("@/conflux/conflux");
import bus from '../../components/bus';
const {Drip} = require("js-conflux-sdk");
export default {
  name: "BlockChainVerification",
  components:{
    pdf
  },
  data() {
    const validateIdNumber = (rule, value, callback) => {
      if(value === ''){
        callback(new Error('请输入证件号码'));
      }
      else {
        if (!isIdCard(value)) {
          callback(new Error('请输入正确的证件号码'));
        }else{
          callback();
        }

      }
    };
    return {
      query: {
        type: '',
        worknname: '',
        pageIndex: 1,
        pageSize: 10
      },

      workTypeOptions: [{
        value: '1',
        label: '摄影作品'
      }, {
        value: '2',
        label: '文字作品'
      }, {
        value: '3',
        label: '影视作品'
      }, {
        value: '4',
        label: '音乐作品'
      }, {
        value: '5',
        label: '美术作品'
      }],

      ownerTypeOptions: [{
        value: '1',
        label: '自然人',
        children:[{
          value: '1',
          label: '身份证号'
        }, {
          value: '2',
          label: '军人身份证明'
        }, {
          value: '3',
          label: '户口本'
        },{
          value: '5',
          label: '其他有效证件'
        }]
      }, {
        value: '2',
        label: '企业法人',
        children: [{
          value: '4',
          label: '统一社会信用代码证书'
        }]
      }, {
        value: '3',
        label: '机关法人',
        children: [{
          value: '4',
          label: '统一社会信用代码证书'
        }]
      }, {
        value: '4',
        label: '事业单位法人',
        children: [{
          value: '4',
          label: '统一社会信用代码证书'
        }]
      }, {
        value: '5',
        label: '社会团体法人',
        children: [{
          value: '4',
          label: '统一社会信用代码证书'
        }]
      },{
        value: '6',
        label: '其他',
        children: [{
          value: '5',
          label: '其他有效证件'
        }]
      }],

      idTypeOptions: [],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
      tableData: [],
      editVisible: false,
      addVisible: false,
      drawer: false,
      pageTotal: 0,
      editForm: {
        workname:'',
        type:'',
        introduction:'',
        completiontime:'',
        completionlocation:'',
        ispublished:'',
        isauthor:'',
        copyrightownertype:'',
        idtype:'',
        copyrightownerid:'',
        copyrightownername:'',
        publishedtime:'',
        publishedlocation:'',
        authorname:'',
        authoridnumber:'',
        isauthority:true,
        id:''
      },
      editRules:{
        workname:[
          { required: true, message: '请输入作品名称', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择作品类型', trigger: 'change' }
        ],
        completiontime: [
          { required: true, message: '请选择完成时间', trigger: 'change' }
        ],
        copyrightownertype: [
          { required: true, message: '请选择类别', trigger: 'change' }
        ],
        publishedtime: [
          { required: true, message: '请选择发表时间', trigger: 'change' }
        ],
        idtype: [
          { required: true, message: '请选择证件类型', trigger: 'change' }
        ],
        ispublished: [
          { required: true, message: '请选择是否发表', trigger: 'change' }
        ],
        isauthor: [
          { required: true, message: '请选择是否为作者', trigger: 'change' }
        ],
        introduction: [
          { min: 0, max: 500, message: '作品介绍长度在需要在 0 到 500 个字符', trigger: 'blur' }
        ],
        completionlocation:[
          { required: true, message: '请输入完成地点', trigger: 'blur' },
          { min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur' }
        ],
        copyrightownerid:[
          { validator:validateIdNumber, trigger: 'blur' },
        ],
        copyrightownername:[
          { required: true, message: '请输入名称', trigger: 'blur' },
          {  min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        publishedlocation:[
          { required: true, message: '请输入发表地点', trigger: 'blur' },
          {  min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        authorname:[
          { required: true, message: '请输入作者姓名', trigger: 'blur' },
          {  min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
        ],
        authoridnumber:[
          { validator:validateIdNumber,trigger: 'blur' },
        ],
      },
      fileList: [],
      fileList1: [],
      userinfo:'',
      drawerMessage:{},
      addForm: {
        workname:'',
        type:'',
        introduction:'',
        completiontime:'',
        completionlocation:'',
        ispublished:'',
        isauthor:'',
        copyrightownertype:'',
        idtype:'',
        copyrightownerid:'',
        copyrightownername:'',
        publishedtime:'',
        publishedlocation:'',
        authorname:'',
        authoridnumber:'',
        userid:'',
        isauthority:true,
      },
      account:'',
      addRules:{
        workname:[
          { required: true, message: '请输入作品名称', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择作品类型', trigger: 'change' }
        ],
        completiontime: [
          { required: true, message: '请选择完成时间', trigger: 'change' }
        ],
        copyrightownertype: [
          { required: true, message: '请选择类别', trigger: 'change' }
        ],
        publishedtime: [
          { required: true, message: '请选择发表时间', trigger: 'change' }
        ],
        idtype: [
          { required: true, message: '请选择证件类型', trigger: 'change' }
        ],
        ispublished: [
          { required: true, message: '请选择是否发表', trigger: 'change' }
        ],
        isauthor: [
          { required: true, message: '请选择是否为作者', trigger: 'change' }
        ],
        introduction: [
          { min: 0, max: 500, message: '作品介绍长度在需要在 0 到 500 个字符', trigger: 'blur' }
        ],
        completionlocation:[
          { required: true, message: '请输入完成地点', trigger: 'blur' },
          { min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur' }
        ],
        copyrightownerid:[
          { validator:validateIdNumber, trigger: 'blur' },
        ],
        copyrightownername:[
          { required: true, message: '请输入名称', trigger: 'blur' },
          {  min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        publishedlocation:[
          { required: true, message: '请输入发表地点', trigger: 'blur' },
          {  min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        authorname:[
          { required: true, message: '请输入作者姓名', trigger: 'blur' },
          {  min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
        ],
        authoridnumber:[
          { validator:validateIdNumber,trigger: 'blur' },
        ],
      },
      idx: -1,
      loading:false,
      addDialogLoading:false
    };
  },
  watch: {

  },
  created() {
    this.userinfo=JSON.parse(sessionStorage.getItem("userInfo"))
    this.getData();
    bus.$on('refreshWorkData', () => {
      console.log("调用咯")
      this.getData();
    })
    console.log(this.userinfo)
  },
  methods: {
    ownerTypeChange(val){
      this.idTypeOptions = []
      this.addForm.idtype = ''
      this.ownerTypeOptions.forEach((item) => {
        if (val === item.value) {
          this.idTypeOptions = item.children
          console.log('idType', this.idTypeOptions)
        }
      })
    },
    getData() {
      this.loading = true
      const userId = this.userinfo.id
      console.log(userId)
      this.$axios.get('/works/getWorksPage',{
        params:{
          currentPage:this.query.pageIndex,
          pageSize:this.query.pageSize,
          userId:userId,
          workname:this.query.worknname,
          type:this.query.type
        }
      }).then(response => {
        if (response.data.code==='200') {
          this.tableData=response.data.result.records
          console.log(this.tableData)
          this.pageTotal=response.data.result.total
          this.loading = false
        } else {
          this.$message({
            message: "获取列表失败",
            type: "error"
          });
          this.loading = false
        }

      }).catch(e => {
        this.$message({
          message: "网络异常！",
          type: "error"
        });
        this.loading = false
      });
    },
    handleAddDialogClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    closeAddDialog(addForm){
      this.$refs['uploadComponent'].clearFiles();
      this.$refs[addForm].resetFields();
      this.addVisible = false;
      this.getData();
    },
    handleEditDialogClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    closeEditDialog(editForm){
      this.editVisible=false;
      this.$refs['uploadComponent1'].clearFiles();
      this.$refs[editForm].resetFields();
      this.getData();
    },

    onBeforeUpload (file) {
      const isLt1GB = file.size / 1024 / 1024 /1024 < 1;
      let isAccept=true;
      let fileSuffix  = file.name.substring(file.name.lastIndexOf(".") + 1);
      const whiteList = ["pdf", "doc", "docx", "xls", "xlsx","txt","ppt","pptx","wmv","asf","asx","rm","rmvb","mp4","3gp","mov","m4v","avi","dat","mkv","flv","vob","mgb","mp3","m4a","rar","zip","tar","img","mpeg","wave","jepg","gif","png","bmp","tif","svg"];
      if (whiteList.indexOf(fileSuffix.toLowerCase()) === -1) {
        this.$message.error('暂不支持该类型文件！');
        isAccept=false;
      }
      if (!isLt1GB) {
        this.$message({
          type: "error",
          message: "上传文件大小不得大于1GB！"
        });
      }
      return isLt1GB && isAccept;
    },

    handleChange(file, fileList) {
      this.fileList = fileList;
    },
    handleRemove (file, fileList) {
      this.$refs['uploadComponent'].clearFiles();
    },
    handlePreview (file) {
      console.log("FILE"+file);
    },
    handleExceed (files, fileList) {
      this.$message.warning(
          `当前限制选择 1 个文件，本次选择了 ${files.length
          } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    beforeRemove (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    handleChange1(file, fileList) {
      this.fileList1 = fileList;
    },
    handleRemove1 (file, fileList) {
      this.$refs['uploadComponent1'].clearFiles();
    },
    handlePreview1 (file) {
      console.log("FILE"+file);
    },
    handleExceed1 (files, fileList) {
      this.$message.warning(
          `当前限制选择 1 个文件，本次选择了 ${files.length
          } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    beforeRemove1 (file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    // 触发搜索按钮
    handleSearch() {
      this.$set(this.query, 'pageIndex', 1);
      this.getData();
    },
    handleRefresh() {
      this.getData();
    },

    changeSelect(){
      this.$set(this.query, 'pageIndex', 1);
      console.log(this.query.type)
      this.getData();
    },

    handleDetail(index, row){
      this.drawerMessage=row;

      console.log(this.drawerMessage)
      this.drawer=true;

    },
    // 删除操作
    handleDelete(index, row) {
      const params=new FormData();
      params.append("id",row.id)
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post(`/works/deleteWorks`, params).then(async response => {
          if (response.data.code === '200') {
            this.$message({
              message: "删除成功了呢",
              type: "success",
            });
            bus.$emit('refreshDashboard');
            this.getData();
          } else {
            this.$message({
              message: "删除失败,请重试",
              type: "error"
            });
          }
        }).catch(e => {
          this.$message({
            message: "网络异常！",
            type: "error"
          });
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },


    // 编辑操作
    handleEdit(index, row) {
      this.idx = index;
      if(row.completiontime!==null)
      {
        row.completiontime=row.completiontime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '');
      }
      this.editForm.authoridnumber = row.authoridnumber;
      this.editForm.authorname = row.authorname;
      this.editForm.completionlocation = row.completionlocation;
      this.editForm.type = row.type;
      this.editForm.idtype=row.idtype;
      this.editForm.completiontime=row.completiontime;
      this.editForm.copyrightownerid=row.copyrightownerid;
      this.editForm.copyrightownername=row.copyrightownername;
      this.editForm.copyrightownertype=row.copyrightownertype;
      this.editForm.introduction=row.introduction;
      this.editForm.isauthor=row.isauthor;
      this.editForm.isauthority=row.isauthority;
      this.editForm.ispublished=row.ispublished;
      this.editForm.publishedlocation=row.publishedlocation;
      this.editForm.publishedtime=row.publishedtime;
      this.editForm.workname=row.workname;
      this.editForm.id=row.id;
      console.log(this.editForm)
      this.editVisible = true;
    },

    // 新增操作
    handleAdd() {
      if(this.userinfo.iscertified===false)
      {
        this.$confirm('你还未实名认证，请前往认证!', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$router.push({ name: "Verification" });
        }).catch(() => {
        });
      }
      else
      {
        this.addVisible = true;
      }

    },
    sleep(time) {
      return new Promise(resolve =>
      setTimeout(resolve,time)
    )},
    //上链存证
    async handleConflux(index, row) {
      await confluxPortal.enable();
      this.account = confluxPortal.getAccount();
      if (this.account === '') {
        this.$message({
          message: '您还未连接conflux钱包插件',
          type: 'warning'
        });
        return false;
      } else {
        this.loading = true;
        const balance=Drip(await ContractHnuCoin.contract.balanceOf(this.account)).toCFX();
        if(balance<10)
        {
          this.$confirm('余额不足', '提示', {
            confirmButtonText: '去充值',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.loading=false;
            this.$router.push({ name: "RechargeCenter" });
            return false;
          }).catch(() => {
            this.loading=false;
            return false;
          });
        }
        else
        {
          //chainhash为空 说明未上链验证，需要进行上链验证的步骤及上链成功后数据写入数据库的步骤
          if(row.chainhash===null)
          {
            console.log("开始上链咯")
            //开始上链
            try {
              console.log("第1部分")
              let amount=Drip.fromCFX(10).toString()
              console.log("第2部分")
              const called = ContractCopyrightSystemTransactions.contract.addWorks.call(row.filehash, row.workuri, row.copyrightownername, row.copyrightownerid,amount)
              console.log(this.account)
              console.log("asdadsadadsas")
              console.log(called.to)
              console.log("asdadsadadsas")
              console.log(called.data)

              const transaction = await confluxPortal.sendTransaction({
                from: this.account,
                to: called.to,
                data: called.data,
              })
              console.log("第4部分")
              const txHash=transaction.result;
              console.log(txHash)
              let out = await this.sleep(3000);
              console.log("延时3s");
              const result = await conflux.getTransactionByHash(txHash);
              console.log(result);
              console.log(result.blockHash)
              const result1=await conflux.getBlockByHash(result.blockHash)
              // console.log(result.epochHeight);
              const blockheight=result.epochHeight;
              const timestamp=result1.timestamp;
              const chaintime=timestamp+"("+this.timestampToTime(timestamp)+")"
              //上链成功,将交易哈希值存入数据库
              console.log("asdasdads")
              row.chainhash=txHash;
              row.blockheight=blockheight;
              row.chaintime=chaintime;
              const params =  new FormData()
              params.append('id',  row.id)
              params.append('chainhash',  txHash)
              params.append('status',  1)
              params.append('blockheight',blockheight)
              params.append('chaintime',chaintime)
              this.$axios.post(`/works/editWorks`,params).then(response => {
                if (response.data.code==='200') {
                  this.$message({
                    message: "权益存证上链成功啦",
                    type: "success"
                  });
                  this.loading = false
                  bus.$emit('refreshGetWorksList');
                  this.getData();
                } else {
                  this.$message({
                    message: "权益存证上链失败，请重试",
                    type: "error"
                  });
                  this.loading = false
                }
              }).catch(e => {
                this.$message({
                  message: "网络异常！",
                  type: "error"
                });
                this.loading = false
              });
            }catch (e){
              //上链失败
              this.$message({
                message: "权益存证上链失败，请重试",
                type: "error"
              });
              console.log(e.message)
              this.loading =false;
            }
          }
          else //chainhash不为空 说明已经上链验证，数据写入数据库失败，需要重新写入
          {console.log("第一部分")
            const params =  new FormData()
            params.append('id',  row.id)
            params.append('chainhash',  row.chainhash)
            params.append('status',  1)
            params.append('blockheight',row.blockheight)
            params.append('chaintime',row.chaintime)
            this.$axios.post(`/works/editWorks`,params).then(response => {
              if (response.data.code==='200') {
                this.$message({
                  message: "权益存证上链成功啦",
                  type: "success"
                });
                this.loading = false
                this.getData();
              } else {
                this.$message({
                  message: "权益存证上链失败，请重试",
                  type: "error"
                });
                this.loading = false
              }
            }).catch(e => {
              this.$message({
                message: "网络异常！",
                type: "error"
              });
              this.loading = false
            });
          }
        }

      }
      console.log(row)
    },
    // 新保存新增
    saveAdd(){
      console.log("新增")
      if (this.fileList.length !== 0 &&!this.onBeforeUpload(this.fileList[0].raw))
      {
        this.fileList.splice(0, 1);
        return false;
      }
      if (this.fileList.length !== 0) {
        this.$refs.addForm.validate( async valid => {
          if (valid) {
            this.addDialogLoading = true

            if (this.addForm.isauthor === true) {
              this.addForm.authoridnumber = this.userinfo.idnumber;
              this.addForm.authorname = this.userinfo.realname;
            }
            if (this.addForm.ispublished === false) {
              this.addForm.publishedtime = '';
              this.addForm.publishedlocation = '';
            }
            this.addForm.userid = this.userinfo.id;
            let formData = new FormData();
            formData.append("file", this.fileList[0].raw)
            for (const key in this.addForm) {
              formData.append(key, this.addForm[key])
            }
            this.$axios.post(`/works/addWorks`, formData).then(async response => {
              if (response.data.code === '200') {
                this.$message({
                  message: "添加成功了呢",
                  type: "success",
                });
                bus.$emit('refreshDashboard');
                this.addDialogLoading = false
                this.addVisible = false;
              } else {
                this.$message({
                  message: "添加失败,请检查输入是否正确",
                  type: "error"
                });
                this.addDialogLoading = false
              }
            }).catch(e => {
              this.$message({
                message: "网络或程序异常！该文件可能已被上传",
                type: "error"
              });
              this.addDialogLoading = false
            });
          } else {
            this.$message({
              message: "请输入合法的值",
              type: "error"
            });
            return false;
          }
        });
      }
      else{
        this.$message.warning("请选择至少一个文件！");
      }
    },

    // 保存编辑
    saveEdit() {
      let formData = new FormData();
      if (this.fileList1.length !== 0 &&!this.onBeforeUpload(this.fileList1[0].raw))
      {
        this.fileList1.splice(0, 1);
        return false;
      }
      if (this.fileList1.length !== 0) {
        formData.append("file",this.fileList1[0].raw)
      }

      this.$refs.editForm.validate(valid => {
        if (valid) {
          this.loading = true
          if(this.editForm.isauthority===true)
          {
            if (this.editForm.ispublished === false) {
              this.editForm.publishedtime = '';
              this.editForm.publishedlocation = '';
            }
          }
          else
          {
            this.editForm.ispublished='';
            this.editForm.isauthor='';
            this.editForm.authorname='';
            this.editForm.authoridnumber='';
            this.editForm.completionlocation='';
            this.editForm.completiontime='';
            this.editForm.publishedtime = '';
            this.editForm.publishedlocation = '';
          }
          for (const key in this.editForm) {
            formData.append(key, this.editForm[key])
          }

          console.log(formData);
          this.$axios.post(`/works/editWorks`,formData).then(response => {
            if (response.data.code==='200') {
              this.$message({
                message: "编辑成功了呢",
                type: "success",
              });
              this.loading = false
              this.editVisible = false
            } else {
              this.$message({
                message: "编辑失败,请检查输入是否正确",
                type: "error"
              });
              this.loading = false
            }

          }).catch(e => {
            this.$message({
              message: "网络异常！",
              type: "error"
            });
            this.loading = false
          });
        } else {
          this.$message({
            message: "请输入合法的值",
            type: "error"
          });
          return false;
        }
      });
    },
    // 分页导航
    handlePageChange(val) {
      this.$set(this.query, 'pageIndex', val);
      this.getData();
    },
    handleCloseDrawer(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    // 时间戳转换成时间
    timestampToTime (cjsj) {
      const date = new Date(cjsj * 1000); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
      const Y = date.getFullYear() + '-'
      const M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-'
      const D = date.getDate() + ' '
      const h = date.getHours() + ':'
      const m = date.getMinutes() + ':'
      const s = date.getSeconds()
      return Y+M+D+h+m+s;
    },


  }
}
</script>

<style scoped>
/deep/ .el-upload{
  width: 100%;
}
/deep/ .el-upload .el-upload-dragger{
  width: 100%;
}

.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 200px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}



</style>