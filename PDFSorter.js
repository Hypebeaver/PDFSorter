// script.js
document.addEventListener('DOMContentLoaded', () => {
  const excelButton = document.getElementById('excelButton');
  const excelInput = document.getElementById('excelInput');
  const removeExcel = document.getElementById('removeExcel');

  const pdfButton = document.getElementById('pdfButton');
  const pdfInput = document.getElementById('pdfInput');
  const removePdf = document.getElementById('removePdf');

  const folderButton = document.getElementById('folderButton');
  const removeFolder = document.getElementById('removeFolder');

  const splitButton = document.getElementById('splitButton');

  let selectedFolderHandle = null;

  function updateButtonState(button, removeButton, hasFile, fileName) {
      button.classList.toggle('green', hasFile);
      button.classList.toggle('blue', !hasFile);
      button.textContent = hasFile ? fileName : button.getAttribute('data-default-text');
      removeButton.style.display = hasFile ? 'inline-block' : 'none';
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
      updateButtonState(excelButton, removeExcel, excelInput.files.length > 0, fileName);
  });
  removeExcel.addEventListener('click', () => updateButtonState(excelButton, removeExcel, false, ''));

  pdfButton.addEventListener('click', () => pdfInput.click());
  pdfInput.addEventListener('change', () => {
      const fileName = pdfInput.files.length > 0 ? pdfInput.files[0].name : '';
      updateButtonState(pdfButton, removePdf, pdfInput.files.length > 0, fileName);
  });
  removePdf.addEventListener('click', () => updateButtonState(pdfButton, removePdf, false, ''));

  folderButton.addEventListener('click', async () => {
      if ('showDirectoryPicker' in window) {
          try {
              selectedFolderHandle = await window.showDirectoryPicker();
              updateButtonState(folderButton, removeFolder, true, selectedFolderHandle.name);
          } catch (err) {
              console.error('Error selecting folder:', err);
              alert('Error selecting folder: ' + err.message);
          }
      } else {
          alert('Your browser does not support folder selection.');
      }
  });

  removeFolder.addEventListener('click', () => {
      selectedFolderHandle = null;
      updateButtonState(folderButton, removeFolder, false, '');
  });
});
