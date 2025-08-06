// ...existing code...

/**
 * 抽取盲盒
 */
router.post('/draw-mystery-box/:id', auth.required, repositoryController.drawMysteryBox);

// ...existing code...
