document.addEventListener("DOMContentLoaded", function () {
  const fileInput = document.getElementById('fileInput');
  const chooseFileBtn = document.querySelector('.chooseFileBtn');
  const removeFileBtn = document.getElementById('removeFileBtn');
  const uploadBtn = document.getElementById('uploadBtn');
  const convertBtn = document.getElementById('convertBtn');
  const downloadBtn = document.getElementById('downloadBtn');
  const fileNameDisplay = document.getElementById('fileName');

  // Function to handle file upload
  function handleFileUpload() {
    if (fileInput.files.length > 0) {
      uploadBtn.removeAttribute('disabled');
      removeFileBtn.removeAttribute('disabled');
      fileNameDisplay.textContent = fileInput.files[0].name;
    } else {
      uploadBtn.setAttribute('disabled', 'disabled');
      convertBtn.setAttribute('disabled', 'disabled');
      downloadBtn.setAttribute('disabled', 'disabled');
      removeFileBtn.setAttribute('disabled', 'disabled');
      fileNameDisplay.textContent = 'No file chosen';
    }
  }

  // Function to handle removing file
  function handleFileRemove() {
    fileInput.value = ''; // Clear the file input
    uploadBtn.setAttribute('disabled', 'disabled');
    convertBtn.setAttribute('disabled', 'disabled');
    downloadBtn.setAttribute('disabled', 'disabled');
    removeFileBtn.setAttribute('disabled', 'disabled');
    fileNameDisplay.textContent = 'No file chosen';
  }

  // Function to simulate file upload
  function uploadFile() {
    // Your upload logic here
    console.log('File uploaded');
    convertBtn.removeAttribute('disabled');
  }

  // Function to simulate file conversion
  function convertFile() {
    // Your conversion logic here
    console.log('File converted');
    downloadBtn.removeAttribute('disabled');
  }

  // Function to simulate file download
  function downloadFile() {
    // Your download logic here
    console.log('File downloaded');
  }

  chooseFileBtn.addEventListener('click', () => {
    fileInput.click(); // Simulate click on file input
  });

  removeFileBtn.addEventListener('click', handleFileRemove);

  fileInput.addEventListener('change', handleFileUpload);

  uploadBtn.addEventListener('click', () => {
    uploadFile();
    convertBtn.removeAttribute('disabled');
  });

  convertBtn.addEventListener('click', convertFile);

  downloadBtn.addEventListener('click', downloadFile);

  // Initially disable all buttons except Choose File and Remove File
  uploadBtn.setAttribute('disabled', 'disabled');
  convertBtn.setAttribute('disabled', 'disabled');
  downloadBtn.setAttribute('disabled', 'disabled');
});
