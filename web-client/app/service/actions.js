export const actions = {
  removeFile: (store) => (item) => {
    store.commit('setFilesRemoving', true);
    return store.serviceFactory.getFileServiceInstance().removeFile(item)
        .then(() => {
          store.commit('setFilesRemoving', false);
          return store.dispatch('fetchFiles');
        }).catch((error) => {
          store.commit('setFilesRemoving', false);
          store.commit('setErrorMessage', error.message);
        });
  },
  shareFile: (store) => (fileId) => {
    return store.serviceFactory.getFileServiceInstance().shareFile(fileId)
        .then((URL) => {
          return URL;
        });
  },
  uploadFile: (store) => (file, folderId) => {
    store.commit('setFilesUploading', true);
    const fileName = file.name;
    const fileReader = new FileReader();

    fileReader.readAsBinaryString(file);

    fileReader.onload = () => {
      const fileContent = fileReader.result;

      return store.serviceFactory.getFileServiceInstance()
          .uploadFile(fileContent, fileName, folderId)
          .then(() => {
            store.commit('setFilesUploading', false);
            return store.dispatch('fetchFiles');
          }).catch((error) => {
            store.commit('setFilesUploading', false);
            store.commit('setErrorMessage', error.message);
          });
    };
  },
  fetchFiles: (store) => () => {
    store.commit('setFilesLoading', true);
    return store.serviceFactory.getFileServiceInstance()
        .getFiles(store.state.currentFolder.recordId.value)
        .then((response) => {
          return response.json().then((body) => {
            store.commit('setFiles', body.folderContent.items);
            if (store.state.selectedFile) {
              store.commit('setSelectedFile',
                  store.state.files.find((file) => file.id === store.state.selectedFile.id));
            }
          });
        })
        .catch((error) => {
          store.commit('setErrorMessage', error.message);
        }).finally(() => {
          store.commit('setFilesLoading', false);
        });
  },
  selectFile: (store) => (file) => {
    return store.commit('setSelectedFile', file);
  },
  renameFile: (store) => (fileId, newFileName) => {
    store.commit('setFileRenaming', true);
    const promise = store.serviceFactory.getFileServiceInstance().renameFile(fileId, newFileName);
    return promise.then(() => {
      store.commit('setFileRenaming', false);
      return store.dispatch('fetchFiles');
    }).catch((error) => {
      store.commit('setFileRenaming', false);
      store.commit('setErrorMessage', error.message);
    });
  },
  navigate: (store) => (folder) => {
    store.commit('setFilesLoading', true);
    return store.serviceFactory.getFileServiceInstance().getFolder(folder.id)
        .then((response) => {
          response.json().then((body) => {
            store.commit('setCurrentFolder', body.folder);
            return store.dispatch('fetchFiles');
          });
        }).catch((error) => {
          store.commit('setErrorMessage', error.message);
        }).finally(() => {
          store.commit('setFilesLoading', false);
        });
  },
  moveFile: (store) => (fileId, folderId) => {
    store.commit('setFilesMoving', true);
    return store.serviceFactory.getFileServiceInstance().moveFile(fileId, folderId)
        .then(() => {
          store.commit('setFilesMoving', false);
          return store.dispatch('fetchFiles');
        }).catch((error) => {
          store.commit('setFilesMoving', false);
          store.commit('setErrorMessage', error.message);
        });
  },
  createFolder: (store) => (newFolderName, parentFolderId) => {
    store.commit('setFolderCreating', true);
    return store.serviceFactory.getFileServiceInstance()
        .createFolder(newFolderName, parentFolderId)
        .then(() => {
          store.commit('setFolderCreating', false);
          store.dispatch('fetchFiles');
        })
        .catch((error) => {
          store.commit('setFolderCreating', false);
          store.commit('setErrorMessage', error.message);
          throw error;
        });
  },
  register: (store) => (user) => {
    return store.serviceFactory.getUserServiceInstance().register(user)
        .catch((error) => {
          store.commit('setErrorMessage', error.message);
          throw error;
        });
  },
  login: (store) => (login, password) => {
    return store.serviceFactory.getUserServiceInstance().logIn({login, password})
        .then((body) => {
          const folder = body.rootFolder;
          folder.name = 'root';
          store.commit('setCurrentFolder', folder);
        })
        .catch((error) => {
          store.commit('setErrorMessage', error.message);
          throw error;
        });
  },
};
