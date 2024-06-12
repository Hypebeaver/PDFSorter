// script.js
document.addEventListener('DOMContentLoaded', () => {
  const excelButton = document.getElementById('excelButton');
  const excelInput = document.getElementById('excelInput');
  const removeExcel = document.getElementById('removeExcel');

  const pdfButton = document.getElementById('pdfButton');
  const pdfInput = document.getElementById('pdfInput');
  const removePdf = document.getElementById('removePdf');

  const folderButton = document.getElementById('folderButton');
  const folderInput = document.getElementById('folderInput');
  const removeFolder = document.getElementById('removeFolder');

  const splitButton = document.getElementById('splitButton');

  function updateButtonState(button, removeButton, input, hasFile, fileName) {
      button.classList.toggle('green', hasFile);
      button.classList.toggle('blue', !hasFile);
      button.textContent = hasFile ? fileName : button.getAttribute('data-default-text');
      removeButton.style.display = hasFile ? 'inline-block' : 'none';
      input.value = '';
      updateSplitButtonState();
  }

  function updateSplitButtonState() {
      const allSelected = [excelButton, pdfButton, folderButton].every(button => button.classList.contains('green'));
      splitButton.disabled = !allSelected;
      splitButton.classList.toggle('grey', !allSelected);
      splitButton.classList.toggle('blue', allSelected);
  }

  excelButton.addEventListener('click', () => excelInput.click());
  excelInput.addEventListener('change', () => {
      const fileName = excelInput.files.length > 0 ? excelInput.files[0].name : '';
      updateButtonState(excelButton, removeExcel, excelInput, excelInput.files.length > 0, fileName);
  });
  removeExcel.addEventListener('click', () => updateButtonState(excelButton, removeExcel, excelInput, false, ''));

  pdfButton.addEventListener('click', () => pdfInput.click());
  pdfInput.addEventListener('change', () => {
      const fileName = pdfInput.files.length > 0 ? pdfInput.files[0].name : '';
      updateButtonState(pdfButton, removePdf, pdfInput, pdfInput.files.length > 0, fileName);
  });
  removePdf.addEventListener('click', () => updateButtonState(pdfButton, removePdf, pdfInput, false, ''));

  folderButton.addEventListener('click', () => folderInput.click());
  folderInput.addEventListener('change', () => {
      const fileName = folderInput.files.length > 0 ? folderInput.files[0].webkitRelativePath.split('/')[0] : '';
      updateButtonState(folderButton, removeFolder, folderInput, folderInput.files.length > 0, fileName);
  });
  removeFolder.addEventListener('click', () => updateButtonState(folderButton, removeFolder, folderInput, false, ''));
});
