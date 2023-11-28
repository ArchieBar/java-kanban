package managerTest;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager>{
    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

}