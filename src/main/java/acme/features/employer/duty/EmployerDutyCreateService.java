
package acme.features.employer.duty;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.jobs.Descriptor;
import acme.entities.jobs.Duty;
import acme.entities.roles.Employer;
import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class EmployerDutyCreateService implements AbstractCreateService<Employer, Duty> {

	@Autowired
	EmployerDutyRepository repository;


	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;
		boolean result = true;

		//Assure this is the owner of the descriptor
		int descriptorId;
		Descriptor descriptor;
		Employer employer;
		Principal principal;

		descriptorId = request.getModel().getInteger("descriptorId");
		descriptor = this.repository.findOneDescriptorById(descriptorId);
		employer = descriptor.getJob().getEmployer();
		principal = request.getPrincipal();

		result = employer.getUserAccount().getId() == principal.getAccountId();

		return result;
	}

	@Override
	public void bind(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "titleDuty", "descriptionDuty", "percentage");

		if (request.isMethod(HttpMethod.GET)) {
			model.setAttribute("descriptorId", request.getModel().getInteger("descriptorId"));
		}
	}

	@Override
	public Duty instantiate(final Request<Duty> request) {

		Duty result;
		result = new Duty();
		Descriptor descriptor;
		descriptor = new Descriptor();

		int descriptorId = request.getModel().getInteger("descriptorId");
		descriptor = this.repository.findOneDescriptorById(descriptorId);

		result.setDescriptor(descriptor);

		return result;
	}

	@Override
	public void validate(final Request<Duty> request, final Duty entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Integer allPercentages = 0;
		Collection<Duty> allDuties;
		boolean notMore100;

		// percentages not greater than 100%
		if (!errors.hasErrors("percentage")) {

			allDuties = this.repository.findManyDutiesByDescriptorId(entity.getDescriptor().getId());
			notMore100 = true;

			if (allDuties != null) {
				for (Duty d : allDuties) {
					allPercentages += d.getPercentage();
				}
			}
			notMore100 = allPercentages + entity.getPercentage() <= 100;
			errors.state(request, notMore100, "percentage", "employer.job.form.error.percentages-more-100");
		}
	}

	@Override
	public void create(final Request<Duty> request, final Duty entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

}
