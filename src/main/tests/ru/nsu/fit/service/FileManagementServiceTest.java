package ru.nsu.fit.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.nsu.fit.database.entities.File;
import ru.nsu.fit.database.entities.Folder;
import ru.nsu.fit.database.entities.User;
import ru.nsu.fit.database.repositories.*;
import ru.nsu.fit.database.types.Role;
import ru.nsu.fit.exception.RegistrationException;
import ru.nsu.fit.mock.*;
import ru.nsu.fit.web.login.AuthData;
import ru.nsu.fit.web.navigation.FileDTO;

import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileManagementServiceTest {
    private FolderRepository folderRepository = new MockFolderRepository();
    private FileRepository fileRepository = new MockFileRepository();
    private UserRepository userRepository = new MockUserRepository();
    private FileDataRepository fileDataRepository = new MockFileDataRepository();
    private NavigationService navigationService = new NavigationService(folderRepository, fileRepository, userRepository, fileDataRepository);
    private User user;
    private File file;
    private TokenRepository tokenRepository = new MockTokenRepository();
    private UserService userService = new UserService(userRepository, tokenRepository);
    private LoadService loadService = new LoadService(navigationService, userService, fileDataRepository);
    private FileManagementService fileManagementService = new FileManagementService(fileRepository, fileDataRepository, folderRepository);

    @Before
    public void before() throws RegistrationException, FileUploadException {
        fileRepository.deleteAll();
        userRepository.deleteAll();
        AuthData authData = new AuthData("default", "default");
        user = userService.register(authData);
        navigationService.initializeFolders();
        file = loadService.uploadFile(new FileDTO(new File("A", user, null, "B", "C", "D", 1, 1, 1)), userService.login(authData).getData());
    }

    @Test
    public void markAsChecked() throws Exception {
        fileManagementService.markAsChecked(file.getId());
        Assert.assertTrue(navigationService.getFileInfo(file.getId()).isChecked());
    }

    @Test
    public void deleteFile() throws Exception {
        Assert.assertEquals(navigationService.getAllFiles().size(), 1);
        fileManagementService.deleteFile(file.getId());
        Assert.assertEquals(navigationService.getAllFiles().size(), 0);
    }
}
