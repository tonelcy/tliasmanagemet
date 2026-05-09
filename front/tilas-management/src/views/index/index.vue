<script setup>
import indexImage from '@/assets/index.png'
</script>

<template>
  <div class="index-container">
    <img :src="indexImage" alt="系统首页" class="index-image" />
  </div>
</template>

<style scoped>
.index-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.index-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
</style>
