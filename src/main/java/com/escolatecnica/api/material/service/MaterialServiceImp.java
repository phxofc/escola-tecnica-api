package com.escolatecnica.api.material.service;

import com.escolatecnica.api.clazz.model.Clazz;
import com.escolatecnica.api.clazz.repository.ClazzRepository;
import com.escolatecnica.api.course.model.Course;
import com.escolatecnica.api.course.repository.CourseRepository;
import com.escolatecnica.api.discipline.model.Discipline;
import com.escolatecnica.api.discipline.repository.DisciplineRepository;
import com.escolatecnica.api.material.model.Material;
import com.escolatecnica.api.material.repository.MaterialRepository;
import com.escolatecnica.api.root.service.FilesStorageService;
import com.escolatecnica.api.root.utils.APIException;
import com.escolatecnica.api.root.utils.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MaterialServiceImp implements MaterialService{

    private final MaterialRepository repository;
    private final DisciplineRepository disciplineRepository;
    private final CourseRepository courseRepository;
    private final ClazzRepository clazzRepository;
    private final FilesStorageService storage;


    @Override
    public boolean create(Material material, MultipartFile multipartFile, UUID organizationId, UUID clazzId, UUID courseId, UUID disciplineId) throws NotFoundException, APIException {
        if (Objects.isNull(material)) throw new APIException("[material] is NULL.");

        validateFields(organizationId, courseId, clazzId,  disciplineId);

        Discipline discipline = disciplineRepository.readByIdAndOrganizationId(disciplineId, organizationId);

        if(Objects.isNull(discipline)) throw new APIException("DISCIPLINE not found with id: "+disciplineId);

        Optional<Course> course = courseRepository.readByIdAndOrganizationId(courseId, organizationId);

        if(course.isEmpty()) throw new APIException("COURSE not found with id: "+courseId);

        Clazz clazz = clazzRepository.findByOrganizationIdAndId(organizationId, clazzId);

        if(Objects.isNull(clazz)) throw new APIException("CLAZZ not found with id: "+clazzId);

        if(Objects.nonNull(multipartFile) && Objects.nonNull(multipartFile.getOriginalFilename())){
            material.setAttachedFileName(multipartFile.getOriginalFilename());
        }

        material.setClazz(clazz);
        material.setDiscipline(discipline);
        material.setCourse(course.get());
        material.setIsActive(Boolean.TRUE);
        material.setCreatedAt(ZonedDateTime.now());
        material.setOrganizationId(organizationId);

        material = repository.save(material);

        if(Objects.nonNull(material.getId()) && Objects.nonNull(multipartFile) && Objects.nonNull(multipartFile.getOriginalFilename())){
            storage.save(multipartFile);
        }

        return Boolean.TRUE;
    }

    @Override
    public List<Material> readAllByTeacher(UUID organizationId, UUID teacherId) throws APIException {
        if (Objects.isNull(teacherId) || Objects.isNull(organizationId)) throw new APIException("[teacher id] or [organization id] is NULL.");
        return repository.findByOrganizationIdAndRegisteredBy(organizationId, teacherId);
    }

    @Override
    public Material readByIdAndOrganizationId(UUID id, UUID organizationId) throws APIException {
        if (Objects.isNull(id) || Objects.isNull(organizationId)) throw new APIException("[material id] or [organization id] is NULL.");
        return repository.readByIdAndOrganizationId(id, organizationId);
    }

    @Override
    public List<Material> readAllByOrganizationAndCourseAndClazzAndDiscipline(UUID organizationId, UUID courseId, UUID clazzId, UUID disciplineId) throws APIException {
        validateFields(organizationId, courseId, clazzId,  disciplineId);
        return repository.findByOrganizationIdAndCourse_IdAndClazz_IdAndDiscipline_Id(organizationId, courseId, clazzId, disciplineId);
    }

    @Override
    public boolean manageTaskSubmission(UUID id, UUID organizationId) throws APIException, NotFoundException {
        if (Objects.isNull(organizationId)) throw new APIException("[organization id] is NULL.");

        if (Objects.isNull(id)) throw new APIException("[material id] is NULL.");

        Material material = readByIdAndOrganizationId(id, organizationId);

        if (Objects.isNull(material)) throw new NotFoundException("Not found material with id: "+id);

        material.setIsActive(!material.getIsActive());
        material.setUpdatedAt(ZonedDateTime.now());

        repository.save(material);

        return Boolean.TRUE;
    }

    @Override
    public boolean delete(UUID id, UUID organizationId) throws APIException, NotFoundException {
        if (Objects.isNull(organizationId)) throw new APIException("[organization id] is NULL.");

        if (Objects.isNull(id)) throw new APIException("[material id] is NULL.");

        Material material = readByIdAndOrganizationId(id, organizationId);

        if (Objects.isNull(material)) throw new NotFoundException("Not found material with id: "+id);

        material.setDeleted(Boolean.TRUE);
        material.setDeletedAt(ZonedDateTime.now());

        repository.save(material);

        return Boolean.TRUE;
    }

    private void validateFields(UUID organizationId, UUID courseId, UUID clazzId, UUID disciplineId) throws APIException{
        if (Objects.isNull(organizationId)) throw new APIException("[organization id] is NULL.");

        if (Objects.isNull(clazzId)) throw new APIException("[clazz id] is NULL.");

        if (Objects.isNull(courseId)) throw new APIException("[course id] is NULL.");

        if (Objects.isNull(disciplineId)) throw new APIException("[discipline id] is NULL.");
    }


}
