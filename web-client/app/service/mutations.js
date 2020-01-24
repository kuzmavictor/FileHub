export const mutations = {
  removeFile(state, id) {
    state.files = state.files.filter((file) => file.id !== id);
  },
  setFilesLoading(state, isLoading) {
    state.filesLoading = isLoading;
  },
  setFileRenaming(state, isRenaming) {
    state.fileRenaming = isRenaming;
  },
  setFilesUploading(state, isUploading) {
    state.filesUploading = isUploading;
  },
  setFilesRemoving(state, isRemoving) {
    state.filesRemoving = isRemoving;
  },
  setFilesMoving(state, isMoving) {
    state.filesMoving = isMoving;
  },
  setFolderCreating(state, isCreating) {
    state.folderCreating = isCreating;
  },
  setFiles(state, files) {
    state.files = files;
  },
  setSelectedFile(state, file) {
    state.selectedFile = file;
  },
  renameFile(state, fileId, newFileName) {
    const files = state.files;

    const file = files.find((file) => {
      return file.id === fileId;
    });
    file.name = newFileName;
    state.files = files;
  },
  deleteSelectedFile(state, fileID) {
    if (!!state.selectedFile && state.selectedFile.id === fileID) {
      state.selectedFile = undefined;
    }
  },
  setCurrentFolder(state, currentFolder) {
    state.currentFolder = currentFolder;
  },
  setErrorMessage(state, errorMessage) {
    state.errorMessage = errorMessage;
  },
  setToken(state, token) {
    state.token = token;
  },
};
