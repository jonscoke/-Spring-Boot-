<template>
  <div class="page-grid">
    <section class="panel">
      <div class="panel__head">
        <h3>管理端</h3>
        <el-tag type="danger" round>ADMIN ONLY</el-tag>
      </div>
      <el-alert
        title="这里预留用户管理、内容审核、统计总览等后台能力。"
        type="warning"
        :closable="false"
        show-icon
      />
    </section>
  </div>
</template>
