package ru.nsu.fit.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.nsu.fit.database.entities.File;
import ru.nsu.fit.database.entities.Folder;
import ru.nsu.fit.database.entities.Token;
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
public class LoadServiceTest {
    private FolderRepository folderRepository = new MockFolderRepository();
    private FileRepository fileRepository = new MockFileRepository();
    private UserRepository userRepository = new MockUserRepository();
    private FileDataRepository fileDataRepository = new MockFileDataRepository();
    private TokenRepository tokenRepository = new MockTokenRepository();
    private UserService userService = new UserService(userRepository, tokenRepository);
    private NavigationService navigationService = new NavigationService(folderRepository, fileRepository, userRepository, fileDataRepository);
    private LoadService loadService = new LoadService(navigationService, userService, fileDataRepository);
    private User user;
    private File file;
    private Token token;

    @Before
    public void before() throws RegistrationException {
        fileRepository.deleteAll();
        userRepository.deleteAll();
        tokenRepository.deleteAll();
        AuthData authData = new AuthData("default", "default");
        user = userService.register(authData);
        token = userService.login(authData);
        navigationService.initializeFolders();
        file = navigationService.createFile(new FileDTO(new File("A", user, null, "B", "C", "D", 1, 1, 1)), navigationService.getFolderInfo(navigationService.getRootFolderId()), user);
    }

    @Test
    public void fileData() throws Exception {
        byte[] data = new byte[]
                {'T', 'e', 's', 't'};

        loadService.uploadFileData(file.getId(), data);
        Assert.assertArrayEquals(data, loadService.getFileData(file));
    }

    @Test
    public void uploadFile() throws Exception {
        File uploadedFile = loadService.uploadFile(new FileDTO(new File("name", null, null, "A", "B", "C", 0, 0, 0)), token.getData());
        Assert.assertEquals(uploadedFile.getName(), "name");
        Assert.assertNotNull(uploadedFile.getId());
    }
}
