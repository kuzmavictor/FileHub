/**
 * Operates RequestService to get files, change files, share or download them.
 */
export class FileService {
  /**
   * @param {RequestService} requestService
   */
  constructor(requestService) {
    this._requestService = requestService;
  }

  /**
   * Sends post request to upload new file.
   *
   * @param {string} fileContent
   * @param {string} fileName
   * @param {string} folderId
   * @return {Promise}
   */
  uploadFile(fileContent, fileName, folderId) {
    const formData = new FormData();
    formData.append('fileName', fileName);
    formData.append('fileContent', fileContent);
    return this._requestService.post('/folder/' + folderId + '/upload', formData);
  }

  /**
   * Sends get request to get file list.
   *
   * @param {string} folderId
   * @return {Promise}
   */
  getFiles(folderId) {
    return this._requestService.get('/folder/' + folderId + '/files');
  }

  /**
   * Sends delete request to remove file from database by file ID.
   *
   * @param {File} item
   * @return {Promise}
   */
  removeFile(item) {
    return this._requestService.delete('/' + item.type + '/' + item.id);
  }

  /**
   * Sends put request to rename file.
   *
   * @param {string} fileId
   * @param {string} newName
   * @return {Promise}
   */
  renameFile(fileId, newName) {
    return this._requestService.put('/files/' + fileId, newName);
  }

  /**
   * Moves file from current folder to another folder.
   *
   * @param {string} fileId
   * @param {string} folderId
   * @return {Promise}
   */
  moveFile(fileId, folderId) {
    return this._requestService.put('/folder/' + folderId + '/move', fileId);
  }

  /**
   * Sends get request to get file`s share link to share it between other users.
   *
   * @param {string} fileId
   * @return {Promise}
   */
  shareFile(fileId) {
    return this._requestService.get('/files/' + fileId + '/shared-link').then(() => {
      return 'share link for file ' + fileId;
    });
  }

  /**
   * Sends post request to create new folder.
   *
   * @param {string} folderName
   * @param {string} folderId
   * @return {Promise}
   */
  createFolder(folderName, folderId) {
    const formData = new FormData();
    formData.append('folderName', folderName);
    return this._requestService.post('/folder/' + folderId + '/new-folder', formData);
  }

  /**
   * Sends get request to get a current folder.
   *
   * @param {string} folderId
   * @return {Promise}
   */
  getFolder(folderId) {
    return this._requestService.get('/folder/' + folderId);
  }
}
